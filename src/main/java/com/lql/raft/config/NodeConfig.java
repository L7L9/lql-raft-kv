package com.lql.raft.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * 当前节点配置
 * @author lql
 * @date 2024/03/14
 */
@Data
@Configuration
public class NodeConfig {
    @Value("${server.port}")
    private String port;

    @Value("${node.cluster.address}")
    private List<String> peerList;

    private String address;

    {
        try {
            address = port + InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
