package com.lql.raft.entity;

import com.lql.raft.constant.NodeStatus;
import com.lql.raft.rpc.ConsistencyServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * node节点：表示当前节点的状态
 * @author lql
 * @date 2024/01/09
 */
@Component
public class Node {
    /**
     * 当前节点的ip地址
     */
    private String ip;

    /**
     * 当前节点的端口
     */
    private String port;

    /**
     * 节点状态(初始运行时为follow状态)
     */
    private int status;

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
     * TODO
     * 持久化属性：从数据库中读取
     * 日志条目：每个条目包含了用于状态机的命令，以及领导人接收到该条目的时候的任期
     * 初始索引为1
     */
    private String[] logs;

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
     * 一致性模块rpc接口
     */
    @Resource
    private ConsistencyServiceImpl consistencyService;

    @PostConstruct
    public void init(){
        // 1.初始化节点状态
        status = NodeStatus.FOLLOW;
    }
}
