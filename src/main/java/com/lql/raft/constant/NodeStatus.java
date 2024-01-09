package com.lql.raft.constant;

/**
 * 节点当前状态的常量类
 *
 * @author lql
 * @date 2024/01/09
 */
public class NodeStatus {
    /**
     * 跟随者状态
     */
    public static int FOLLOW = 0;

    /**
     * 候选人状态
     */
    public static int CANDIDATE = 1;

    /**
     * leader节点
     */
    public static int LEADER = 2;
}
