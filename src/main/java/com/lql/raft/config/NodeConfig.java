package com.lql.raft.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 当前节点配置
 * @author lql
 * @date 2024/03/14
 */
@Data
@Configuration
public class NodeConfig {
    @Value("${node.id}")
    private String nodeId;

    @Value("${node.cluster.address}")
    private List<String> peerList;
}
