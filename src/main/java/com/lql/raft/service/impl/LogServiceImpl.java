package com.lql.raft.service.impl;

import com.alibaba.fastjson2.JSON;
import com.lql.raft.constant.DirConstants;
import com.lql.raft.entity.LogEntity;
import com.lql.raft.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 日志服务实现类
 * @author lql
 * @date 2024/03/14
 */
@Slf4j
public class LogServiceImpl implements LogService {
    /**
     * 最后索引的key
     */
    private final static byte[] LAST_KEY = "last_key".getBytes();

    /**
     * 锁的ttl
     */
    private final static int LOCK_TTL = 5000;

    /**
     * 写删锁，防止同时写和删除
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * log数据库
     */
    private RocksDB logDb;

    private LogServiceImpl(){}

    private static LogServiceImpl instance = new LogServiceImpl();

    public static LogServiceImpl getInstance(){
        if(instance == null){
            synchronized (LogServiceImpl.class){
                if(instance == null){
                    instance = new LogServiceImpl();
                }
            }
        }

        return instance;
    }

    public void init(String port){
        // 初始化rocksdb
        String databaseDir = DirConstants.ROOT_DIR + port;
        String logDir = databaseDir + "/log-data";

        RocksDB.loadLibrary();
        Options options = new Options();
        options.setCreateIfMissing(true);
        // 判断目录是否存在，不存在则创建
        File file = new File(logDir);
        if(!file.exists()){
            if(file.mkdirs()){
                log.info("create a new mkdir:" + logDir);
            }
        }
        try {
            this.logDb = RocksDB.open(options,logDir);
        } catch (RocksDBException e) {
            log.warn(e.getMessage());
        }
    }

    public void destroy(){
        // 关闭数据库
        logDb.close();
    }

    @Override
    public void write(LogEntity logEntity) {
        try {
            // 写入时要进行加锁,防止多线程更改lastIndex
            if(!lock.tryLock(LOCK_TTL, TimeUnit.MILLISECONDS)){
                throw new RuntimeException("lock failed");
            }

            Long currentIndex = getLastIndex() + 1L;
            logEntity.setIndex(currentIndex);
            // 放入数据
            byte[] data = JSON.toJSONBytes(logEntity);
            logDb.put(currentIndex.toString().getBytes(),data);
            // 将最大索引值加一
            logDb.put(LAST_KEY, currentIndex.toString().getBytes());
        } catch (RocksDBException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public LogEntity get(Long index) {
        try {
            byte[] result = logDb.get(index.toString().getBytes());
            if(result == null){
                return null;
            }
            return JSON.parseObject(result, LogEntity.class);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public LogEntity getLast() {
        return get(getLastIndex());
    }

    @Override
    public Long getLastIndex() {
        Long lastIndex = -1L;
        try {
            byte[] result = logDb.get(LAST_KEY);
            if(result != null){
                lastIndex = JSON.parseObject(result,Long.class);
            }
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
        return lastIndex;
    }

    @Override
    public void delete(Long index) {
        try {
            logDb.delete(index.toString().getBytes());
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }
}
