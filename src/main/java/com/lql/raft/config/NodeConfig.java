package com.lql.raft.config;

import com.lql.raft.entity.Peer;
import com.lql.raft.utils.StringUtils;
import lombok.Data;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * 当前节点配置
 * @author lql
 * @date 2024/03/14
 */
@Data
public class NodeConfig {
    private String port;

    private Integer grpcPort;

    private String address;

    private Set<Peer> peerSet;

    public void init() throws UnknownHostException {
        Yaml yaml = new Yaml();

        InputStream in = this.getClass().getClassLoader().getResourceAsStream("config.yml");
        Map<String, Object> map = yaml.load(in);

        Integer tempPort = Integer.valueOf(System.getProperty("server.port"));
        this.port = String.valueOf(tempPort);
        this.grpcPort = tempPort + 1000;
        this.address = InetAddress.getLocalHost().getHostAddress() + ":" + grpcPort;

        this.peerSet = new HashSet<>();
        List<String> cluster = (List<String>)map.get("cluster");
        for(String addr : cluster){
            if(addr.startsWith(StringUtils.IP_PREFIX)){
                addr = InetAddress.getLocalHost().getHostAddress() + addr.substring(StringUtils.IP_PREFIX.length());
            }
            if(!addr.equals(address)){
                Peer peer = new Peer(addr);
                peerSet.add(peer);
            }
        }
    }
}
