package com.lql.raft.entity;

import com.lql.raft.config.NodeConfig;
import com.lql.raft.constant.NodeStatus;
import com.lql.raft.manager.GrpcServerManager;
import com.lql.raft.rpc.proto.AppendEntriesParam;
import com.lql.raft.rpc.proto.AppendEntriesResponse;
import com.lql.raft.rpc.proto.Log;
import com.lql.raft.service.LogService;
import com.lql.raft.service.impl.LogServiceImpl;
import com.lql.raft.thread.ThreadPoolFactory;
import com.lql.raft.thread.task.ElectoralTask;
import com.lql.raft.thread.task.HeartBeatTask;
import com.lql.raft.utils.ConvertUtils;
import com.lql.raft.utils.TimeUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;

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

    public void init(){
        // 1.初始化节点状态
        status = NodeStatus.FOLLOW;

        // 2.获取持久化属性
        logService = LogServiceImpl.getInstance();
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
}
