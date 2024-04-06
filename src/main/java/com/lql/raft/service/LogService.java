package com.lql.raft.service;

import com.lql.raft.entity.LogEntity;

import java.util.concurrent.Future;

/**
 * 日志模块服务接口
 *
 * @author lql
 * @date 2024/03/14
 */
public interface LogService {
    /**
     * 写入日志实体
     * @param logEntity 日志实体类
     */
    void write(LogEntity logEntity);

    /**
     * 根据索引获取rocksdb中对应的日志实体信息
     * @param index 索引
     * @return 日志实体类
     */
    LogEntity get(Long index);

    /**
     * 获取最后一条日志
     * @return 日志实体类
     */
    LogEntity getLast();

    /**
     * 获取最后一条日志的索引
     * @return 索引值，没有返回-1
     */
    Long getLastIndex();


    /**
     * 从参数开始的index删除到lastIndex
     * @param firstIndex 初始index
     */
    void deleteFromFirstIndex(Long firstIndex);
}
