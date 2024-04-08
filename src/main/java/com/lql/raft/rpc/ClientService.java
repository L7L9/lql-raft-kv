package com.lql.raft.rpc;

import com.lql.raft.constant.ClientRequestType;
import com.lql.raft.constant.NodeStatus;
import com.lql.raft.entity.LogEntity;
import com.lql.raft.entity.Node;
import com.lql.raft.entity.Operation;
import com.lql.raft.entity.Peer;
import com.lql.raft.manager.GrpcServerManager;
import com.lql.raft.rpc.proto.ClientParam;
import com.lql.raft.rpc.proto.ClientResponse;
import com.lql.raft.rpc.proto.ClientServiceGrpc;
import com.lql.raft.service.LogService;
import com.lql.raft.service.StateMachineService;
import com.lql.raft.service.impl.LogServiceImpl;
import com.lql.raft.service.impl.StateMachineServiceImpl;
import com.lql.raft.utils.StringUtils;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

/**
 * client客户端服务:向外部提供查询value和追加日志的功能
 * @author lql
 * @date 2024/03/29
 */
@Slf4j
public class ClientService extends ClientServiceGrpc.ClientServiceImplBase {
    private final Node node;

    private final LogService logService;

    private final StateMachineService stateMachineService;

    public ClientService(Node node){
        this.node = node;
        logService = LogServiceImpl.getInstance();
        stateMachineService = StateMachineServiceImpl.getInstance();
    }

    private final ReentrantLock requestLock = new ReentrantLock();

    @Override
    public void clientRequest(ClientParam request, StreamObserver<ClientResponse> responseObserver) {
        if(!requestLock.tryLock()){
            responseObserver.onNext(ClientResponse.newBuilder().setIsSuccess(false).build());
            responseObserver.onCompleted();
            return;
        }
        try{
            // 判断是否为leader,不是则转发请求到leader中
            if(node.getStatus() != NodeStatus.LEADER){
                responseObserver.onNext(redirect(request,node.getNodeConfig().getLeaderAddress()));
                responseObserver.onCompleted();
                return;
            }
            ClientResponse.Builder builder = ClientResponse.newBuilder().setIsSuccess(false);
            if(request.getRequestType() == ClientRequestType.PUT){
                // 修改数据请求
                if(dealPutRequest(request)){
                    responseObserver.onNext(builder.setIsSuccess(true).setMessage("修改数据成功").build());
                    responseObserver.onCompleted();
                } else {
                    responseObserver.onNext(builder.setMessage("修改数据失败").build());
                    responseObserver.onCompleted();
                }
            } else if(request.getRequestType() == ClientRequestType.GET){
                // 查询请求
                String value = stateMachineService.get(request.getKey());
                if(StringUtils.isEmpty(value)){
                      builder.setMessage("对应键的值不存在");
                      responseObserver.onNext(builder.build());
                      responseObserver.onCompleted();
                      return;
                }
                responseObserver.onNext(builder.setIsSuccess(true).setData(value).setMessage("查询成功").build());
                responseObserver.onCompleted();
            }
        } finally {
            requestLock.unlock();
        }
    }

    /**
     * 重定向rpc请求到对应address
     *
     * @param request rpc请求
     * @param address rpc地址
     * @return Protobuftype rpc响应
     */
    private ClientResponse redirect(ClientParam request,String address){
        return GrpcServerManager.getInstance().getClientServiceBlockingStub(address)
                .clientRequest(request);
    }

    /**
     * 处理put亲贵
     * @param request rpc请求参数
     * @return boolean 成功返回true
     */
    private boolean dealPutRequest(ClientParam request){
        if(StringUtils.isEmpty(request.getKey()) || StringUtils.isEmpty(request.getValue())){
            return false;
        }
        // 包装请求为logEntity
        Operation operation = new Operation().setKey(request.getKey()).setValue(request.getValue());
        LogEntity logEntity = new LogEntity().setTerm(node.getCurrentTerm()).setOperation(operation);
        // 预提交
        logService.write(logEntity);

        List<Future<Boolean>> futureList = new ArrayList<>();
        int count = 0;
        for(Peer peer:node.getNodeConfig().getPeerSet()){
            ++count;
            Future<Boolean> result = node.replication(peer, logEntity);
            futureList.add(result);
        }

        int successCount = node.dealWithReplicationResult(futureList, logEntity);
        if(successCount >= (count / 2)){
            node.setCommitIndex(logEntity.getIndex());
            stateMachineService.commit(logEntity);
            node.setLastApplied(logEntity.getIndex());
            log.info("logEntity successfully commit to state machine,logEntity info: {}",logEntity);
        } else {
            // 回滚之前的日志
            logService.deleteFromFirstIndex(logEntity.getIndex());
            log.warn("logEntity fail to commit,logEntity info: {}",logEntity);
            // TODO 重试日志
        }
        return true;
    }
}
