package com.lql.raft.thread.task;

import com.lql.raft.constant.NodeStatus;
import com.lql.raft.entity.Node;
import com.lql.raft.entity.Peer;
import com.lql.raft.factory.GrpcServerFactory;
import com.lql.raft.rpc.proto.AppendEntriesParam;
import com.lql.raft.rpc.proto.AppendEntriesResponse;
import com.lql.raft.thread.ThreadPoolFactory;
import com.lql.raft.utils.StringUtils;
import com.lql.raft.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 心跳机制任务
 *
 * @author lql
 * @date 2024/03/15
 */
@Slf4j
public class HeartBeatTask implements Runnable{
    /**
     * 心跳间隔
     */
    private static final long HEART_BEAT_TICK = 500;

    /**
     * 获取node节点信息
     */
    private final Node node;

    public HeartBeatTask(Node node){
        this.node = node;
    }

    @Override
    public void run() {
        if(node.getStatus() == NodeStatus.FOLLOW){
            return;
        }
        if(TimeUtils.currentTime() - node.getPreHeartBeatTime() < HEART_BEAT_TICK){
            return;
        }

        node.setPreHeartBeatTime(TimeUtils.currentTime());
        GrpcServerFactory factory = GrpcServerFactory.getInstance();
        for(Peer peer:node.getNodeConfig().getPeerSet()){
            AppendEntriesParam param = AppendEntriesParam.newBuilder()
                    .setLeaderId(node.getNodeConfig().getAddress())
                    .setTerm(node.getCurrentTerm())
                    .setLeaderCommit(node.getCommitIndex())
                    .build();

            ThreadPoolFactory.execute(()->{
                AppendEntriesResponse appendEntriesResponse = factory.getConsistencyServiceBlockingStub(peer.getAddr()).appendEntriesRequest(param);

                // 处理结果
                long term = appendEntriesResponse.getTerm();
                // 任期大于当前节点任期,说明已经不再是leader
                if(term > node.getCurrentTerm()){
                    log.warn("node {} become follower,old term={},new term={}",node.getNodeConfig().getAddress(),node.getCurrentTerm(),term);
                    node.setCurrentTerm(term);
                    node.setStatus(NodeStatus.FOLLOW);
                    node.setVotedFor(StringUtils.EMPTY_STR);
                }
            });
        }
    }
}
