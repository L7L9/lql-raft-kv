package com.lql.raft.rpc;

import com.lql.raft.constant.NodeStatus;
import com.lql.raft.entity.LogEntity;
import com.lql.raft.entity.Node;
import com.lql.raft.entity.Operation;
import com.lql.raft.rpc.proto.*;
import com.lql.raft.service.LogService;
import com.lql.raft.service.StateMachineService;
import com.lql.raft.service.impl.LogServiceImpl;
import com.lql.raft.service.impl.StateMachineServiceImpl;
import com.lql.raft.utils.StringUtils;
import com.lql.raft.utils.TimeUtils;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Consistency接口的实现类
 *
 * @author lql
 * @date 2024/01/18
 */
@Slf4j
public class ConsistencyService extends ConsistencyServiceGrpc.ConsistencyServiceImplBase {
    private final Node node;

    private final LogService logService;

    private final StateMachineService stateMachineService;

    public ConsistencyService(Node node){
        this.node = node;
        logService = LogServiceImpl.getInstance();
        stateMachineService = StateMachineServiceImpl.getInstance();
    }

    /**
     * 投票请求锁
     */
    private final ReentrantLock voteLock = new ReentrantLock();

    /**
     * 追加日志锁,只允许该节点同一时间只处理一个请求
     * 防止以下情况:
     * 有两个leader节点之间还未通信,然后同时向该节点发送了追加日志请求
     */
    private final ReentrantLock appendLock = new ReentrantLock();

    @Override
    public void voteRequest(VoteParam request, StreamObserver<VoteResponse> responseObserver) {
        VoteResponse.Builder response = VoteResponse.newBuilder().setVoteGranted(false);
        if (!voteLock.tryLock()) {
            responseObserver.onNext(response.setTerm(node.getCurrentTerm()).build());
            responseObserver.onCompleted();
            return;
        }
        try {
            // 判断当前节点是否任期比他新
            if (node.getCurrentTerm() > request.getTerm()) {
                response.setTerm(node.getCurrentTerm());
                return;
            }

            if (StringUtils.isEmpty(node.getVotedFor()) || node.getVotedFor().equals(request.getCandidateId())) {
                LogEntity logEntity = logService.getLast();
                if (logEntity != null) {
                    if(logEntity.getTerm() > request.getLastLogTerm()){
                        return;
                    }

                    if (logService.getLastIndex() > request.getLastLogIndex()) {
                        return;
                    }
                }
                node.setStatus(NodeStatus.FOLLOW);
                node.setVotedFor(request.getCandidateId());
                node.setCurrentTerm(request.getTerm());
                node.getNodeConfig().setLeaderAddress(request.getCandidateId());
                response.setVoteGranted(true).setTerm(node.getCurrentTerm());
            }
        } finally {
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
            voteLock.unlock();
        }
    }

    @Override
    public void appendEntriesRequest(AppendEntriesParam request, StreamObserver<AppendEntriesResponse> responseObserver) {
        AppendEntriesResponse.Builder response = AppendEntriesResponse.newBuilder().setSuccess(false);
        // 获取不到锁返回false
        if(!appendLock.tryLock()){
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
            return;
        }
        try{
            response.setTerm(node.getCurrentTerm());
            // 如果当前节点大于请求节点的任期,返回false
            if(node.getCurrentTerm() > request.getTerm()){
                return;
            } else{
                // 由于当前节点有可能为其他状态，在自身任期小于等于发送请求的节点任期时，将自身状态转为follower
                node.setStatus(NodeStatus.FOLLOW);
            }

            // 重置计时器
            node.setPreHeartBeatTime(TimeUtils.currentTime());
            node.setPreElectionTime(TimeUtils.currentTime());
            node.setCurrentTerm(request.getTerm());
            // 判断是否为心跳,为心跳则没有携带新日志
            if(request.getEntriesCount() == 0){
                long nextCommit = node.getCommitIndex() + 1;
                // 日志提交到状态机，前提是leader已经把该日志提交了
                if(request.getLeaderCommit() > node.getCommitIndex()){
                    // 将commitIndex设置为leaderCommit和自身日志最后一个index的较小值
                    long minCommitIndex = Math.min(request.getLeaderCommit(), logService.getLastIndex());
                    node.setCommitIndex(minCommitIndex);
                    node.setLastApplied(minCommitIndex);

                }
                while(nextCommit <= node.getCommitIndex()){
                    stateMachineService.commit(logService.get(nextCommit));
                    ++nextCommit;
                }
                log.info("node receive heart beat,address: {},from address: {}",node.getNodeConfig().getAddress(),request.getLeaderId());
                response.setTerm(node.getCurrentTerm()).setSuccess(true);
                node.setVotedFor(StringUtils.EMPTY_STR);
                return;
            }

            if(logService.getLastIndex() != 0 && request.getPreLogIndex() != 0){
                LogEntity logEntity;
                if((logEntity = logService.get(request.getPreLogIndex())) != null){
                    if(logEntity.getTerm() != request.getPreLogTerm()){
                        return;
                    }
                }
                return;
            }

            // 判断是否有条目在prevLogIndex上能和prevLogTerm匹配上
            LogEntity logEntity = logService.get(request.getPreLogIndex() + 1);
            if(!Objects.isNull(logEntity) && !logEntity.getTerm().equals(request.getPreLogTerm())){
                logService.deleteFromFirstIndex(logEntity.getIndex());
            } else if(!Objects.isNull(logEntity)){
                //已经有日志
                response.setSuccess(true);
                return;
            }

            List<Log> logList = request.getEntriesList();
            // 追加未存在的新条目
            for(Log log:logList){
                Operation operation = null;
                if(log.hasOperate()){
                    operation = new Operation();
                    operation.setKey(log.getOperate().getKey());
                    operation.setKey(log.getOperate().getValue());
                }
                logService.write(new LogEntity()
                        .setIndex(log.getIndex())
                        .setTerm(log.getTerm())
                        .setOperation(operation));
            }
            response.setSuccess(true);
            long nextCommitIndex = node.getCommitIndex() + 1;
            // 设置条目索引
            if(request.getLeaderCommit() > node.getCommitIndex()){
                long min = Math.min(request.getLeaderCommit(),logService.getLastIndex());
                node.setCommitIndex(min);
            }
            // 提交日志
            while(nextCommitIndex <= node.getCommitIndex()){
                stateMachineService.commit(logService.get(nextCommitIndex));
                ++nextCommitIndex;
            }
            response.setTerm(node.getCurrentTerm());
            node.setStatus(NodeStatus.FOLLOW);
        } finally {
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
            appendLock.unlock();
        }
    }
}
