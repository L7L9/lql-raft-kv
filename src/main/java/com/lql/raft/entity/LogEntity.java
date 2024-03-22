package com.lql.raft.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 日志实体类
 * @author lql
 * @date 2024/03/14
 */
@Accessors(chain = true)
@Data
public class LogEntity {
    private Long index;

    private Long term;

    private Operation operation;
}
