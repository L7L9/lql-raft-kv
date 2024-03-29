package com.lql.raft.service.impl;

import com.alibaba.fastjson2.JSON;
import com.lql.raft.config.NodeConfig;
import com.lql.raft.constant.DirConstants;
import com.lql.raft.entity.LogEntity;
import com.lql.raft.entity.Operation;
import com.lql.raft.service.StateMachineService;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.io.File;
import java.util.Objects;

/**
 * 状态机接口实现类
 * @author lql
 * @date 2024/03/22
 */
@Slf4j
public class StateMachineServiceImpl implements StateMachineService {
    private NodeConfig nodeConfig;

    private RocksDB stateDb;

    private StateMachineServiceImpl(){}

    private static StateMachineServiceImpl instance;

    public static StateMachineServiceImpl getInstance(){
        if(instance == null){
            synchronized (StateMachineServiceImpl.class){
                if(instance == null){
                    instance = new StateMachineServiceImpl();
                }
            }
        }

        return instance;
    }

    public void init(String port){
        // 初始化rocksdb
        String databaseDir = DirConstants.ROOT_DIR + port;
        String stateMachineDir = databaseDir + "/stateMachine-data";

        RocksDB.loadLibrary();
        Options options = new Options();
        options.setCreateIfMissing(true);
        // 判断目录是否存在，不存在则创建
        File file = new File(stateMachineDir);
        if(!file.exists()){
            if(file.mkdirs()){
                log.info("create a new mkdir:" + stateMachineDir);
            }
        }
        try {
            stateDb = RocksDB.open(options,stateMachineDir);
        } catch (RocksDBException e) {
            log.warn(e.getMessage());
        }
    }

    public void destroy(){
        stateDb.close();
    }

    @Override
    public synchronized void commit(LogEntity logEntity) {
        Operation operation = logEntity.getOperation();
        if(Objects.isNull(operation)){
           log.info("no operate in log,log index: {}",logEntity.getIndex());
        }
        try {
            stateDb.put(operation.getKey().getBytes(), JSON.toJSONBytes(logEntity));
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String get(String key) {
        String value = null;
        try {
            byte[] result = stateDb.get(key.getBytes());
            if(result != null){
                LogEntity logEntity = JSON.parseObject(result, LogEntity.class);
                value = logEntity.getOperation().getValue();
            }
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
        return value;
    }

    @Override
    public boolean delete(String key) {
        try {
            stateDb.delete(key.getBytes());
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean update(String key, String value) {
        try {
            stateDb.put(key.getBytes(),value.getBytes());
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
