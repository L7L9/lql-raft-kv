package com.lql.raft.rpc;

import com.lql.raft.config.NodeConfig;
import com.lql.raft.constant.NodeStatus;
import com.lql.raft.entity.LogEntity;
import com.lql.raft.entity.Node;
import com.lql.raft.rpc.proto.*;
import com.lql.raft.service.LogService;
import com.lql.raft.utils.StringUtils;
import com.lql.raft.utils.TimeUtils;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import javax.annotation.Resource;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Consistency接口的实现类
 *
 * @author lql
 * @date 2024/01/18
 */
@GrpcService
public class ConsistencyService extends ConsistencyServiceGrpc.ConsistencyServiceImplBase {
    @Resource
    private Node node;

    @Resource
    private NodeConfig nodeConfig;

    @Resource
    private LogService logService;

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
        if(voteLock.tryLock()){
            // 判断当前节点是否任期比他新
            if(node.getCurrentTerm() > request.getTerm()){
                responseObserver.onNext(response.build());
                responseObserver.onCompleted();
                voteLock.unlock();
                return;
            }
            if(StringUtils.isEmpty(node.getVotedFor()) || node.getVotedFor().equals(request.getCandidateId())){
                LogEntity logEntity = logService.getLast();
                if(logEntity != null && logEntity.getTerm() > request.getLastLogTerm()){
                    responseObserver.onNext(response.build());
                    responseObserver.onCompleted();
                    voteLock.unlock();
                    return;
                }
                if(logService.getLastIndex() > request.getLastLogIndex()){
                    responseObserver.onNext(response.build());
                    responseObserver.onCompleted();
                    voteLock.unlock();
                    return;
                }
                node.setStatus(NodeStatus.FOLLOW);
                node.setVotedFor(request.getCandidateId());
                node.setCurrentTerm(request.getTerm());

                response.setVoteGranted(true).setTerm(node.getCurrentTerm());
                responseObserver.onNext(response.build());
                responseObserver.onCompleted();
                voteLock.unlock();
                return;
            }
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void appendEntriesRequest(AppendEntriesParam request, StreamObserver<AppendEntriesResponse> responseObserver) {
        AppendEntriesResponse.Builder response = AppendEntriesResponse.newBuilder();
        response.setSuccess(false);
        // 获取不到锁返回false
        if(!appendLock.tryLock()){
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
        }

        response.setTerm(node.getCurrentTerm());
        // 如果当前节点大于请求节点的任期,返回false
        if(node.getCurrentTerm() > request.getTerm()){
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
        }

        node.setPreHeartBeatTime(TimeUtils.currentTime());
        node.setPreElectionTime(TimeUtils.currentTime());
        // 判断是否为心跳,为心跳则没有携带新日志
        if(request.getEntriesCount() == 0){





        }
    }
}
