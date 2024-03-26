package com.lql.raft.factory;

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
public class GrpcServerFactory {
    private HashMap<String,ConsistencyServiceGrpc.ConsistencyServiceBlockingStub>  stubMap = new HashMap<>();

    private GrpcServerFactory(){
    }

    private static volatile GrpcServerFactory instance;

    public static GrpcServerFactory getInstance(){
        if(instance == null){
            synchronized (GrpcServerFactory.class){
                if(instance == null){
                    instance = new GrpcServerFactory();
                }
            }
        }

        return instance;
    }

    /**
     * 开启当前节点的grpc服务
     * @throws IOException 异常
     */
    public void initAndStart(int port,ConsistencyService consistencyService) throws IOException {
        Server server = ServerBuilder.forPort(port).addService(consistencyService).build();
        server.start();
        log.info("grpc server start success,port: {}",port);
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
}
