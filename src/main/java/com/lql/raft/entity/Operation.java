package com.lql.raft.entity;

import lombok.Data;

/**
 * 操作实体类
 * 记录
 * @author lql
 * @date 2024/03/14
 */
@Data
public class Operation {
    private String key;

    private String value;
}
