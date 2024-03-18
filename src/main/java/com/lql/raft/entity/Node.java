package com.lql.raft.entity;

import com.lql.raft.config.NodeConfig;
import com.lql.raft.constant.NodeStatus;
import com.lql.raft.rpc.ConsistencyService;
import com.lql.raft.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * node节点：表示当前节点的状态
 * @author lql
 * @date 2024/01/09
 */
@Slf4j
@Component
public class Node {
    @Resource
    private NodeConfig nodeConfig;

    /**
     * 节点状态(初始运行时为follow状态)
     */
    private volatile int status;

    /**
     * 非持久化属性：该节点已知的提交的最高日志条目的索引值
     * 初始值为0，单调递增
     */
    private long commitIndex;

    /**
     * 非持久化属性：该节点已被应用到状态机中的日志条目的索引值
     * 初始值为0，单调递增
     */
    private long lastApplied;

    /**
     * 持久化属性：从数据库中读取
     * 服务器已知的最新的任期
     * 初始值为0
     */
    private long currentTerm;

    /**
     * 持久化属性：从数据库中读取
     * 当前任期内收到选票的candidateId
     * 如果没有投给任何候选人则为空
     */
    private String votedFor;

    /**
     * 持久化属性：从数据库中读取
     * 日志条目：每个条目包含了用于状态机的命令，以及领导人接收到该条目的时候的任期
     * 初始索引为1
     */
    @Resource
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
     * 上次响应其他节点和选举的时间戳
     */
    private long lastResponseTime = 0L;

    /**
     * 一致性模块rpc接口
     */
    @Resource
    private ConsistencyService consistencyService;

    @PostConstruct
    public void init(){
        // 1.初始化节点状态
        status = NodeStatus.FOLLOW;

        // 2.获取持久化属性
        LogEntity logEntity = logService.getLast();
        currentTerm = logEntity == null?0:logEntity.getTerm();

        // 3.开启定时心跳

        // 4.开启超时检测,一段时间接收不到心跳后发起投票选举


        log.info("raft-node start success,node-address: {}",nodeConfig.getAddress());
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCommitIndex() {
        return commitIndex;
    }

    public void setCommitIndex(long commitIndex) {
        this.commitIndex = commitIndex;
    }

    public long getLastApplied() {
        return lastApplied;
    }

    public void setLastApplied(long lastApplied) {
        this.lastApplied = lastApplied;
    }

    public long getCurrentTerm() {
        return currentTerm;
    }

    public void setCurrentTerm(long currentTerm) {
        this.currentTerm = currentTerm;
    }

    public String getVotedFor() {
        return votedFor;
    }

    public void setVotedFor(String votedFor) {
        this.votedFor = votedFor;
    }

    public Map<Peer, Long> getNextIndex() {
        return nextIndex;
    }

    public void setNextIndex(Map<Peer, Long> nextIndex) {
        this.nextIndex = nextIndex;
    }

    public Map<Peer, Long> getMatchIndex() {
        return matchIndex;
    }

    public void setMatchIndex(Map<Peer, Long> matchIndex) {
        this.matchIndex = matchIndex;
    }

    public long getLastResponseTime() {
        return lastResponseTime;
    }

    public void setLastResponseTime(long lastResponseTime) {
        this.lastResponseTime = lastResponseTime;
    }
}
