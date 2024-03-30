package com.lql.raft.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 操作实体类
 * 记录
 * @author lql
 * @date 2024/03/14
 */
@Accessors(chain = true)
@Data
public class Operation {
    private String key;

    private String value;
}
