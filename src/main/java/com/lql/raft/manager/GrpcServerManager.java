package com.lql.raft.manager;

import com.lql.raft.config.NodeConfig;
import com.lql.raft.entity.Peer;
import com.lql.raft.rpc.ConsistencyService;
import com.lql.raft.rpc.proto.ConsistencyServiceGrpc;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;

/**
 * GRPC服务工厂
 *
 * @author lql
 * @date 2024/03/17
 */
@Slf4j
public class GrpcServerManager {
    private NodeConfig nodeConfig;

    private final HashMap<String,ConsistencyServiceGrpc.ConsistencyServiceBlockingStub>  stubMap = new HashMap<>();

    private GrpcServerManager(){
    }

    private static volatile GrpcServerManager instance;

    public static GrpcServerManager getInstance(){
        if(instance == null){
            synchronized (GrpcServerManager.class){
                if(instance == null){
                    instance = new GrpcServerManager();
                }
            }
        }

        return instance;
    }

    /**
     * 开启当前节点的grpc服务
     * @throws IOException 异常
     */
    public void initAndStart(NodeConfig nodeConfig,ConsistencyService consistencyService) throws IOException {
        this.nodeConfig = nodeConfig;
        Server server = ServerBuilder.forPort(nodeConfig.getPort()).addService(consistencyService).build();
        server.start();
        log.info("grpc server start success,port: {}",nodeConfig.getPort());
    }

    /**
     * 获取对应服务端的stub
     * @param address 节点地址
     * @return rpc stub
     */
    public ConsistencyServiceGrpc.ConsistencyServiceBlockingStub getConsistencyServiceBlockingStub(String address){
        if(!stubMap.containsKey(address)){
            ManagedChannel managedChannel = ManagedChannelBuilder.forTarget(address)
                    .usePlaintext()
                    .build();

            stubMap.put(address,ConsistencyServiceGrpc.newBlockingStub(managedChannel));
        }
        return stubMap.get(address);
    }
//
//    /**
//     * 删除对应stub
//     * @param address 节点地址
//     */
//    public void removeClient(String address){
//        nodeConfig.getPeerSet().remove(new Peer(address));
//        stubMap.remove(address);
//    }
}
