package com.lql.raft.utils;

import com.lql.raft.entity.LogEntity;
import com.lql.raft.entity.Operation;
import com.lql.raft.rpc.proto.Log;
import com.lql.raft.rpc.proto.Operate;

import java.util.Objects;

/**
 * 转换工具类
 * @author lql
 * @date 2024/03/26
 */
public class ConvertUtils {
    /**
     * LogEntity转化为protobuf中的log
     * @param logEntity 日志类
     * @return Protobuftype Log类
     */
    public static Log logEntityToLog(LogEntity logEntity){
        if(Objects.isNull(logEntity)){
            return null;
        }
        Operation operation= logEntity.getOperation();
        Operate operate = null;
        if(!Objects.isNull(operation)){
            operate = Operate.newBuilder().setKey(operation.getKey()).setValue(operation.getValue()).build();
        }

        return Log.newBuilder()
                .setTerm(logEntity.getTerm())
                .setIndex(logEntity.getIndex())
                .setOperate(operate).build();
    }
}
