package com.lql.raft.exception;

/**
 * raft异常
 *
 * @author lql
 * @date 2024/01/11
 */
public class RaftException extends RuntimeException{
    public RaftException(String message) {
        super(message);
    }
}
