package com.lql.raft.entity;

import java.util.Objects;

/**
 * 用于表示当前节点连接的其他节点
 * @author lql
 * @date 2024/01/19
 */
public class Peer {
    /**
     * 节点的ip:port
     */
    private String addr;

    public Peer(String addr){
        this.addr = addr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Peer peer = (Peer) o;
        return addr.equals(peer.addr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addr);
    }
}
