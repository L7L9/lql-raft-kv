package com.lql.raft.entity;

import lombok.Data;

/**
 * 日志实体类
 * @author lql
 * @date 2024/03/14
 */
@Data
public class LogEntity {
    private Long index;

    private Long term;

    private Operation operation;
}
