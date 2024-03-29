package com.lql.raft.manager;

import com.lql.raft.config.NodeConfig;
import com.lql.raft.rpc.ConsistencyService;
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
    private NodeConfig nodeConfig;

    private Server server;

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
    public void initAndStart(NodeConfig nodeConfig,io.grpc.BindableService... services) throws IOException {
        this.nodeConfig = nodeConfig;
        ServerBuilder<?> builder = ServerBuilder.forPort(nodeConfig.getPort());
        for(io.grpc.BindableService service:services){
            builder.addService(service);
        }
        this.server = builder.build();
        this.server.start();
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

    /**
     * 关闭当前节点rpc服务
     */
    public void destroy(){
        this.server.shutdown();
    }
}
