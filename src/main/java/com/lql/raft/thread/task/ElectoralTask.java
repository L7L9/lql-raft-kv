package com.lql.raft.thread.task;

import com.lql.raft.constant.NodeStatus;
import com.lql.raft.entity.LogEntity;
import com.lql.raft.entity.Node;
import com.lql.raft.entity.Peer;
import com.lql.raft.manager.GrpcServerManager;
import com.lql.raft.rpc.proto.VoteParam;
import com.lql.raft.rpc.proto.VoteResponse;
import com.lql.raft.service.LogService;
import com.lql.raft.service.StateMachineService;
import com.lql.raft.service.impl.LogServiceImpl;
import com.lql.raft.service.impl.StateMachineServiceImpl;
import com.lql.raft.thread.ThreadPoolFactory;
import com.lql.raft.utils.StringUtils;
import com.lql.raft.utils.TimeUtils;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 选举任务线程类
 * @author lql
 * @date 2024/03/16
 */
@Slf4j
public class ElectoralTask implements Runnable{
    /**
     * 获取node节点信息
     */
    private final Node node;

    private final LogService logService = LogServiceImpl.getInstance();

    private final StateMachineService stateMachineService = StateMachineServiceImpl.getInstance();

    /**
     * 选举间隔时间
     */
    private volatile long electionInterval = 15 * 1000;

    public ElectoralTask(Node node){
        this.node = node;
    }

    @Override
    public void run() {
        if(node.getStatus() == NodeStatus.LEADER){
            return;
        }

        long current = TimeUtils.currentTime();
        electionInterval = electionInterval + ThreadLocalRandom.current().nextInt(50);
        // 判断时间间隔是否小于选举间隔时间
        if(current - node.getPreElectionTime() < electionInterval){
            return;
        }
        log.warn("node will become candidate and start election,address: {}",node.getNodeConfig().getAddress());
        // 状态变为获选人
        node.setStatus(NodeStatus.CANDIDATE);
        node.setPreElectionTime(TimeUtils.currentTime() + ThreadLocalRandom.current().nextInt(150) + 150);

        node.setVotedFor(node.getNodeConfig().getAddress());
        long currentTerm = node.getCurrentTerm() + 1;
        node.setCurrentTerm(currentTerm);

        // 用于接收调用结果
        List<Future<VoteResponse>> futureList = new ArrayList<>();
        GrpcServerManager factory = GrpcServerManager.getInstance();
        // 遍历同伴节点并且发送请求投票rpc
        for(Peer peer : node.getNodeConfig().getPeerSet()){
            Future<VoteResponse> result = ThreadPoolFactory.submit(() -> {
                long term = 0L;
                LogEntity logEntity = logService.getLast();
                if(!Objects.isNull(logEntity)){
                    term = logEntity.getTerm();
                }
                // 发送投票rpc
                VoteParam voteParam = VoteParam.newBuilder()
                        .setCandidateId(node.getNodeConfig().getAddress())
                        .setTerm(currentTerm)
                        .setLastLogIndex(logService.getLastIndex())
                        .setLastLogTerm(term).build();
                VoteResponse voteResponse = null;
                try{
                    voteResponse = factory.getConsistencyServiceBlockingStub(peer.getAddr()).voteRequest(voteParam);
                } catch (StatusRuntimeException e){
                    return null;
                }

                return voteResponse;

            });
            futureList.add(result);
        }

        // 用于记录投票给自己的节点个数
        AtomicInteger count = new AtomicInteger(0);
        // 用于等待所有线程处理完成
        CountDownLatch countDownLatch = new CountDownLatch(node.getNodeConfig().getPeerSet().size());
        // 接收结果并且处理
        for(Future<VoteResponse> result: futureList){
            ThreadPoolFactory.submit(()->{
                try {
                    // 获取结果
                    VoteResponse voteResponse = result.get(3000, TimeUnit.MILLISECONDS);
                    if(Objects.isNull(voteResponse)){
                        return -1;
                    }
                    if(voteResponse.getVoteGranted()){
                        count.incrementAndGet();
                    } else {
                        // 对方节点不同意的情况,说明对面可能是领导节点
                        // 比较任期,如果任期大于自身任期则更新
                        if(voteResponse.getTerm() >= node.getCurrentTerm()){
                            node.setCurrentTerm(voteResponse.getTerm());
                        }
                    }
                    return 0;
                } catch (Exception e) {
                    log.error("future get fail,reason: {}",e.getMessage());
                    return -1;
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        try {
            // 阻塞等待上面线程处理完结果
            if(!countDownLatch.await(3500,TimeUnit.MILLISECONDS)){
                log.error("some vote rpc response overtime");
            }
        } catch (InterruptedException e) {
            log.error("countDownLatch InterruptedException: {}",e.getMessage());
        }
        // 在处理过程中,有可能因为收到心跳而从candidate变回follower,则停止处理
        if(node.getStatus() == NodeStatus.FOLLOW){
            return;
        }

        int voteCount = count.get();
        // 投票总数大于等于所有节点数量的一半即转变为leader
        if(voteCount >= (node.getNodeConfig().getPeerSet().size() + 1) / 2){
            log.info("new node become leader,address: {}",node.getNodeConfig().getAddress());
            node.setStatus(NodeStatus.LEADER);
            node.setVotedFor(StringUtils.EMPTY_STR);
            afterBecomeLeader();
        } else {
            node.setVotedFor(StringUtils.EMPTY_STR);
        }
        node.setPreElectionTime(TimeUtils.currentTime() + ThreadLocalRandom.current().nextInt(150) + 150);
    }

    /**
     * 成为leader后节点需要更新的事务
     */
    private void afterBecomeLeader(){
        // 初始化nextIndex和MatchIndex
        int size = node.getNodeConfig().getPeerSet().size();
        node.setNextIndex(new ConcurrentHashMap<>(size));
        node.setMatchIndex(new ConcurrentHashMap<>(size));
        long nextIndex = logService.getLastIndex() + 1;
        for(Peer peer:node.getNodeConfig().getPeerSet()){
            node.getNextIndex().put(peer, nextIndex);
            node.getMatchIndex().put(peer,0L);
        }

        // 发送空日志提交，用于提交处理之前的日志
        LogEntity logEntity = new LogEntity().setTerm(node.getCurrentTerm());
        // 预提交
        logService.write(logEntity);

        // 记录响应节点
        int count = 0;
        List<Future<Boolean>> futureList = new ArrayList<>();
        for(Peer peer:node.getNodeConfig().getPeerSet()){
            ++count;
            Future<Boolean> result = node.replication(peer, logEntity);
            futureList.add(result);
        }

        int successCount = node.dealWithReplicationResult(futureList, logEntity);

        if(successCount >= (count / 2)){
            node.setCommitIndex(logEntity.getIndex());
            stateMachineService.commit(logEntity);
            node.setLastApplied(logEntity.getIndex());
            log.info("logEntity successfully commit to state machine,logEntity info: {}",logEntity);
        } else {
            // 回滚之前的日志
            logService.deleteFromFirstIndex(logEntity.getIndex());
            log.warn("logEntity fail to commit,logEntity info: {}",logEntity);

            log.warn("node: {} become leader fail",node.getNodeConfig().getAddress());
            node.setStatus(NodeStatus.FOLLOW);
            node.setVotedFor(StringUtils.EMPTY_STR);
            node.getNodeConfig().setLeaderAddress(StringUtils.EMPTY_STR);
        }
    }
}
