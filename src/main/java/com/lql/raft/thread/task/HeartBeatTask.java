package com.lql.raft.thread.task;

import com.lql.raft.config.NodeConfig;
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
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 心跳机制任务
 *
 * @author lql
 * @date 2024/03/15
 */
@Slf4j
@Component
public class HeartBeatTask implements Runnable{
    /**
     * 心跳间隔
     */
    private static final long HEART_BEAT_TICK = 500;

    /**
     * 获取node节点信息
     */
    @Resource
    private Node node;

    @Resource
    private NodeConfig nodeConfig;

    @Override
    public void run() {
        if(node.getStatus() == NodeStatus.FOLLOW){
            return;
        }
        if(TimeUtils.currentTime() - node.getPreHeartBeatTime() < HEART_BEAT_TICK){
            return;
        }

        node.setPreHeartBeatTime(TimeUtils.currentTime());

        for(Peer peer:nodeConfig.getPeerSet()){
            AppendEntriesParam param = AppendEntriesParam.newBuilder()
                    .setLeaderId(nodeConfig.getAddress())
                    .setTerm(node.getCurrentTerm())
                    .setLeaderCommit(node.getCommitIndex())
                    .build();

            ThreadPoolFactory.execute(()->{
                AppendEntriesResponse appendEntriesResponse = GrpcServerFactory.getConsistencyServiceBlockingStub(peer.getAddr()).appendEntriesRequest(param);

                // 处理结果
                long term = appendEntriesResponse.getTerm();
                // 任期大于当前节点任期,说明已经不再是leader
                if(term > node.getCurrentTerm()){
                    log.warn("node {} become follower,old term={},new term={}",nodeConfig.getAddress(),node.getCurrentTerm(),term);
                    node.setCurrentTerm(term);
                    node.setStatus(NodeStatus.FOLLOW);
                    node.setVotedFor(StringUtils.EMPTY_STR);
                }
            });
        }
    }
}
