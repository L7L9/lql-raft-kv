package com.lql.raft.service;

import com.lql.raft.entity.LogEntity;

/**
 * 状态机接口
 *
 * @author lql
 * @date 2024/03/22
 */
public interface StateMachineService {
    /**
     * 提交日志到状态机
     * @param logEntity 日志类
     */
    void commit(LogEntity logEntity);

    /**
     * 通过key获取对应value
     * @param key 键
     * @return 字符串类型的值
     */
    String get(String key);

    /**
     * 删除key对应键值对
     * @param key 键
     * @return boolean 成功返回true
     */
    boolean delete(String key);

    /**
     * 根据对应key更改对应value
     * @param key 键
     * @param value 值
     * @return boolean 成功返回true
     */
    boolean update(String key,String value);
}
