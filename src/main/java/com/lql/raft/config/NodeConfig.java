package com.lql.raft.config;

import com.lql.raft.entity.Peer;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 当前节点配置
 * @author lql
 * @date 2024/03/14
 */
@Setter
@Configuration
public class NodeConfig {
    @Value("${server.port}")
    private String port;

    @Value("${node.cluster.address}")
    private List<String> peerList;

    private String address;

    private Set<Peer> peerSet;

    {
        peerSet = new HashSet<>();
        try {
            address = port + InetAddress.getLocalHost().getHostAddress();
            for(String addr:peerList){
                if(!addr.equals(address)){
                    peerSet.add(new Peer(addr));
                }
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAddress() {
        return address;
    }

    public Set<Peer> getPeerSet() {
        return peerSet;
    }
}
