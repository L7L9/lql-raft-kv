package com.lql.raft.factory;

import com.lql.raft.rpc.ConsistencyService;
import com.lql.raft.rpc.proto.ConsistencyServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;

/**
 * GRPC服务工厂
 *
 * @author lql
 * @date 2024/03/17
 */
@Slf4j
@Component
public class GrpcServerFactory {
    @Value("${grpc.server.port}")
    private int grpcPort;

    @Resource
    private ConsistencyService consistencyService;

    private static HashMap<String,ConsistencyServiceGrpc.ConsistencyServiceBlockingStub>  stubMap = new HashMap<>();

    /**
     * 开启当前节点的grpc服务
     * @throws IOException 异常
     */
    @PostConstruct
    public void startServer() throws IOException {
        Server server = ServerBuilder.forPort(grpcPort).addService(consistencyService).build();
        server.start();
        log.info("grpc server start success,port: {}",grpcPort);
    }

    /**
     * 获取对应服务端的stub
     * @param address 节点地址
     * @return rpc stub
     */
    public static ConsistencyServiceGrpc.ConsistencyServiceBlockingStub getConsistencyServiceBlockingStub(String address){
        if(!stubMap.containsKey(address)){
            ManagedChannel managedChannel = ManagedChannelBuilder.forTarget(address)
                    .usePlaintext()
                    .build();

            stubMap.put(address,ConsistencyServiceGrpc.newBlockingStub(managedChannel));
        }
        return stubMap.get(address);
    }
}
