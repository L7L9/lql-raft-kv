package com.lql.raft;

import com.lql.raft.config.NodeConfig;
import com.lql.raft.entity.Node;
import com.lql.raft.manager.GrpcServerManager;
import com.lql.raft.rpc.ConsistencyService;
import com.lql.raft.service.impl.LogServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * @author lql
 * @date 2024/01/09
 */
@Slf4j
public class NodeApplication {
    public static void main(String[] args) throws InterruptedException {
        // 1.配置初始化
        NodeConfig nodeConfig = new NodeConfig();
        Node node = new Node();
        try {
            // 初始化
            nodeConfig.init();

            // 2.初始化服务
            LogServiceImpl.getInstance().init(String.valueOf(nodeConfig.getPort()));

            // 3.初始化node
            node.setNodeConfig(nodeConfig);
            // 4.开启grpc服务
            GrpcServerManager factory = GrpcServerManager.getInstance();
            factory.initAndStart(nodeConfig, new ConsistencyService(node));

            // 5.启动
            node.init();
        } catch (UnknownHostException e) {
            log.error("config init error,reason: {}",e.getMessage());
        } catch (IOException e) {
            log.error("grpc server start failed,reason: {}",e.getMessage());
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            synchronized (node) {
                node.notifyAll();
            }
        }));

        log.info("gracefully wait");

        synchronized (node) {
            node.wait();
        }

        LogServiceImpl.getInstance().destroy();
        GrpcServerManager.getInstance().destroy();
    }
}
