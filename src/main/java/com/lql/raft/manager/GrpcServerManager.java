package com.lql.raft.manager;

import com.lql.raft.config.NodeConfig;
import com.lql.raft.entity.Peer;
import com.lql.raft.rpc.proto.ClientServiceGrpc;
import com.lql.raft.rpc.proto.ConsistencyServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
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
    private Server server;

    private final HashMap<String,ConsistencyServiceGrpc.ConsistencyServiceBlockingStub> consistencyServiceBlockingStubHashMap = new HashMap<>();

    private final HashMap<String, ClientServiceGrpc.ClientServiceBlockingStub> clientServiceBlockingStubHashMap = new HashMap<>();
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
    public void initAndStart(NodeConfig nodeConfig,io.grpc.BindableService... services) throws IOException {
        // 初始化服务端
        ServerBuilder<?> builder = ServerBuilder.forPort(nodeConfig.getPort());
        for(io.grpc.BindableService service:services){
            builder.addService(service);
        }
        this.server = builder.build();
        this.server.start();
        log.info("grpc server start success,port: {}",nodeConfig.getPort());

        // 初始化stub
        for(Peer peer: nodeConfig.getPeerSet()){
            String address = peer.getAddr();
            ManagedChannel managedChannel = ManagedChannelBuilder.forTarget(address)
                    .usePlaintext()
                    .build();
            // 初始化consistencyService
            consistencyServiceBlockingStubHashMap.put(address, ConsistencyServiceGrpc.newBlockingStub(managedChannel));
            // 初始化clientService
            clientServiceBlockingStubHashMap.put(address, ClientServiceGrpc.newBlockingStub(managedChannel));
            log.info("grpc client init success,client-address: {}",address);
        }
    }

    /**
     * 获取一致性服务的stub
     * @param address 节点地址
     * @return consistencyServiceBlockingStub
     */
    public ConsistencyServiceGrpc.ConsistencyServiceBlockingStub getConsistencyServiceBlockingStub(String address){
        return consistencyServiceBlockingStubHashMap.get(address);
    }

    /**
     * 获取client服务的stub
     * @param address 节点地址
     * @return clientServiceBlockingStub
     */
    public ClientServiceGrpc.ClientServiceBlockingStub getClientServiceBlockingStub(String address){
        return clientServiceBlockingStubHashMap.get(address);
    }

    /**
     * 关闭当前节点rpc服务
     */
    public void destroy(){
        this.server.shutdown();
    }
}
