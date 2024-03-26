package com.lql.raft.thread.task;

import com.lql.raft.constant.NodeStatus;
import com.lql.raft.entity.Node;
import com.lql.raft.entity.Peer;
import com.lql.raft.factory.GrpcServerFactory;
import com.lql.raft.rpc.proto.VoteParam;
import com.lql.raft.rpc.proto.VoteResponse;
import com.lql.raft.service.LogService;
import com.lql.raft.service.impl.LogServiceImpl;
import com.lql.raft.thread.ThreadPoolFactory;
import com.lql.raft.utils.StringUtils;
import com.lql.raft.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
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

    /**
     * 选举间隔时间
     */
    private final static long ELECTION_INTERVAL = 20 * 1000;

    public ElectoralTask(Node node){
        this.node = node;
    }

    @Override
    public void run() {
        if(node.getStatus() == NodeStatus.LEADER){
            return;
        }

        long current = TimeUtils.currentTime();
        // 判断时间间隔是否小于选举间隔时间
        if(current - node.getPreElectionTime() < ELECTION_INTERVAL){
            return;
        }
        // 状态变为获选人
        node.setStatus(NodeStatus.CANDIDATE);
        node.setPreElectionTime(TimeUtils.currentTime());

        node.setVotedFor(node.getNodeConfig().getAddress());
        long currentTerm = node.getCurrentTerm() + 1;
        node.setCurrentTerm(currentTerm);

        // 用于接收调用结果
        List<Future<VoteResponse>> futureList = new ArrayList<>();
        GrpcServerFactory factory = GrpcServerFactory.getInstance();
        // 遍历同伴节点并且发送请求投票rpc
        for(Peer peer : node.getNodeConfig().getPeerSet()){
            Future<VoteResponse> result = ThreadPoolFactory.submit(() -> {
                // 发送投票rpc
                VoteParam voteParam = VoteParam.newBuilder()
                        .setCandidateId(node.getNodeConfig().getAddress())
                        .setTerm(currentTerm)
                        .setLastLogIndex(logService.getLastIndex())
                        .setLastLogTerm(logService.getLast().getTerm()).build();

                VoteResponse voteResponse = factory.getConsistencyServiceBlockingStub(peer.getAddr()).voteRequest(voteParam);
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
            ThreadPoolFactory.execute(()->{
                try {
                    // 获取结果
                    VoteResponse voteResponse = result.get(3000, TimeUnit.MILLISECONDS);
                    if(Objects.isNull(voteResponse)){
                        return;
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
                } catch (Exception e) {
                    log.error("future get fail,reason: {}",e.getMessage());
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        try {
            // 阻塞等待上面线程处理完结果
            if(!countDownLatch.await(3000,TimeUnit.MILLISECONDS)){
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
            afterBecomeLeader();
        }
        node.setVotedFor(StringUtils.EMPTY_STR);
        node.setPreElectionTime(TimeUtils.currentTime());
    }

    /**
     * 成为leader后节点需要更新的事务
     */
    private void afterBecomeLeader(){
        int size = node.getNodeConfig().getPeerSet().size();
        node.setNextIndex(new ConcurrentHashMap<>(size));
        node.setMatchIndex(new ConcurrentHashMap<>(size));

        for(Peer peer:node.getNodeConfig().getPeerSet()){
            node.getNextIndex().put(peer, logService.getLastIndex() + 1);
            node.getMatchIndex().put(peer,0L);
        }


    }
}
