package com.lql.raft.entity;

import com.lql.raft.config.NodeConfig;
import com.lql.raft.constant.NodeStatus;
import com.lql.raft.service.LogService;
import com.lql.raft.service.impl.LogServiceImpl;
import com.lql.raft.thread.ThreadPoolFactory;
import com.lql.raft.thread.task.ElectoralTask;
import com.lql.raft.thread.task.HeartBeatTask;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

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
}
