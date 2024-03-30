package com.lql.raft.entity;

import com.lql.raft.config.NodeConfig;
import com.lql.raft.constant.NodeStatus;
import com.lql.raft.manager.GrpcServerManager;
import com.lql.raft.rpc.proto.AppendEntriesParam;
import com.lql.raft.rpc.proto.AppendEntriesResponse;
import com.lql.raft.rpc.proto.Log;
import com.lql.raft.service.LogService;
import com.lql.raft.service.StateMachineService;
import com.lql.raft.service.impl.LogServiceImpl;
import com.lql.raft.service.impl.StateMachineServiceImpl;
import com.lql.raft.thread.ThreadPoolFactory;
import com.lql.raft.thread.task.ElectoralTask;
import com.lql.raft.thread.task.HeartBeatTask;
import com.lql.raft.utils.ConvertUtils;
import com.lql.raft.utils.StringUtils;
import com.lql.raft.utils.TimeUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * node节点：表示当前节点的状态
 * @author lql
 * @date 2024/01/09
 */
@Slf4j
@Data
public class Node {
    /**
     * 节点配置
     */
    private NodeConfig nodeConfig;

    /**
     * 节点状态(初始运行时为follow状态)
     */
    private volatile int status;

    /**
     * 非持久化属性：该节点已知的提交的最高日志条目的索引值
     * 初始值为0，单调递增
     */
    private volatile long commitIndex;

    /**
     * 非持久化属性：该节点已被应用到状态机中的日志条目的索引值
     * 初始值为0，单调递增
     */
    private volatile long lastApplied = 0;

    /**
     * 持久化属性：从数据库中读取
     * 服务器已知的最新的任期
     * 初始值为0
     */
    private volatile long currentTerm;

    /**
     * 持久化属性：从数据库中读取
     * 当前任期内收到选票的candidateId
     * 如果没有投给任何候选人则为空
     */
    private volatile String votedFor;

    /**
     * 持久化属性：从数据库中读取
     * 日志条目：每个条目包含了用于状态机的命令，以及领导人接收到该条目的时候的任期
     * 初始索引为1
     */
    private LogService logService;

    /**
     * 当为领导人节点时的非持久属性
     * 当前节点要发送给它的伙伴节点的下一条日志条目的索引值
     * 初始值：领导节点最后的日志条目索引 + 1
     */
    private Map<Peer,Long> nextIndex;

    /**
     * 当为领导人节点时的非持久属性
     * 对于当前节点，已知其他节点复制了当前节点的最高日志条目的索引
     * 初始值为0，单调递增
     */
    private Map<Peer,Long> matchIndex;

    /**
     * 上次选举的时间戳
     */
    private volatile long preElectionTime = 0L;

    /**
     * 上次心跳的时间戳
     */
    private volatile long preHeartBeatTime = 0L;

    /**
     * 日志复制的ttl
     */
    private final static int REPLICATION_TTL = 20 * 1000;

    private StateMachineService stateMachineService;

    public void init(){
        // 1.初始化节点状态
        status = NodeStatus.FOLLOW;

        // 2.获取持久化属性
        logService = LogServiceImpl.getInstance();
        stateMachineService = StateMachineServiceImpl.getInstance();
        LogEntity logEntity = logService.getLast();
        currentTerm = logEntity == null?0:logEntity.getTerm();

        // 3.开启定时心跳
        ThreadPoolFactory.scheduleWithFixedDelay(new HeartBeatTask(this),500);
        // 4.开启超时检测,一段时间接收不到心跳后发起投票选举
        ThreadPoolFactory.scheduleAtFixedRate(new ElectoralTask(this),6000,500);

        log.info("raft-node start success,node-address: {}",nodeConfig.getAddress());
    }

    /**
     * 日志复制
     * @param logEntity 日志
     * @return 成功返回true
     */
    public synchronized Future<Boolean> replication(Peer peer,LogEntity logEntity){
        return ThreadPoolFactory.submit(()->{
            long start = TimeUtils.currentTime();
            long end = start;

            // 一定时间内可以重试
            while(end - start <= REPLICATION_TTL){
                AppendEntriesParam.Builder builder = AppendEntriesParam.newBuilder();

                //设置追加日志rpc请求参数
                builder.setTerm(this.getCurrentTerm());
                builder.setLeaderId(nodeConfig.getAddress());
                builder.setLeaderCommit(this.getCommitIndex());

                Long nextIndexTemp = this.nextIndex.get(peer);
                List<Log> logList = new ArrayList<>();
                if(logEntity.getIndex() >= nextIndexTemp){
                    for(long i = nextIndexTemp;i <= logEntity.getIndex();i++){
                        LogEntity temp = logService.get(i);
                        if(Objects.nonNull(temp)){
                            logList.add(ConvertUtils.logEntityToLog(temp));
                        }
                    }
                } else {
                    // 包装
                    logList.add(ConvertUtils.logEntityToLog(logEntity));
                }

                // 获取最小日志
                LogEntity preLogEntity = logService.get(logList.get(0).getIndex());
                if(Objects.isNull(preLogEntity)){
                    builder.setPreLogIndex(0L).setPreLogTerm(0L);
                } else {
                    builder.setPreLogIndex(preLogEntity.getIndex()).setPreLogTerm(preLogEntity.getTerm());
                }
                AppendEntriesParam param = builder.addAllEntries(logList).build();

                AppendEntriesResponse response = GrpcServerManager.getInstance()
                        .getConsistencyServiceBlockingStub(peer.getAddr()).appendEntriesRequest(param);

                // 处理结果
                if (response.getSuccess()){
                    // 成功，更新matchIndex和nextIndex
                    this.matchIndex.put(peer, logEntity.getIndex());
                    this.nextIndex.put(peer, logEntity.getIndex() + 1L);
                    return true;
                } else{
                    // 检测任期
                    if(response.getTerm() > this.currentTerm){
                        log.warn("node[{}] term[{}] greater than my term[{}],",peer.getAddr(),response.getTerm(),this.currentTerm);
                        this.currentTerm = response.getTerm();
                        this.status = NodeStatus.FOLLOW;
                        return false;
                    } else {
                        if(nextIndexTemp == 0L){
                            ++nextIndexTemp;
                        }
                        nextIndex.put(peer,nextIndexTemp - 1);
                        log.warn("follower address:{}, nextIndex do not match",peer.getAddr());
                    }
                }
                end = TimeUtils.currentTime();
            }

            return false;
        });
    }

    public void dealWithReplicationResult(List<Future<Boolean>> futureList,LogEntity logEntity,int responseCount){
        CountDownLatch latch = new CountDownLatch(futureList.size());
        AtomicInteger successCount = new AtomicInteger(0);
        // 处理结果
        for(Future<Boolean> future:futureList){
            ThreadPoolFactory.execute(()->{
                try {
                    if (future.get(3000, TimeUnit.MILLISECONDS)){
                        // 记录成功个数
                        successCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    log.error("exception happen while get replication result,reason: {}",e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await(4000,TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        // 需要找到一个满足条件的最大索引midIndex,使得超过一半节点的matchIndex都大于等于midIndex，
        // 并且在midIndex处的日志条目的任期号与当前任期号相同。
        // 找到这样一个midIndex可以确保在当前任期号下,大多数节点都已经复制了该日志条目之前的所有日志条目,并且已经持久化到了存储中。
        // 而中位数刚好可以保证
        List<Long> matchIndexTemp = new ArrayList<>(this.getMatchIndex().values());
        if(matchIndexTemp.size() >= 2){
            Collections.sort(matchIndexTemp);
        }
        Long midIndex = matchIndexTemp.get(matchIndexTemp.size() / 2);
        if(midIndex > this.getCommitIndex()){
            // 判断任期是否相同
            LogEntity logTemp = logService.get(midIndex);
            if(Objects.nonNull(logTemp) && logTemp.getTerm() == this.getCurrentTerm()){
                this.setCommitIndex(midIndex);
            }
        }

        if(successCount.get() >= (responseCount / 2)){
            this.setCommitIndex(logEntity.getIndex());
            stateMachineService.commit(logEntity);
            this.setLastApplied(logEntity.getIndex());
            log.info("logEntity successfully commit to state machine,logEntity info: {}",logEntity);
        } else {
            // 回滚之前的日志
            Long endIndex = logService.getLastIndex();
            for(long i = logEntity.getIndex();i < endIndex;i++){
                logService.delete(i);
            }
            log.warn("logEntity fail to commit,logEntity info: {}",logEntity);

            log.warn("node: {} become leader fail",this.getNodeConfig().getAddress());
            this.setStatus(NodeStatus.FOLLOW);
            this.setVotedFor(StringUtils.EMPTY_STR);
        }
    }
}
