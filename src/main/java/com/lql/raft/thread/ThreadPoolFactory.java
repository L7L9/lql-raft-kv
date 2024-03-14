package com.lql.raft.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 线程池工厂
 *
 * @author lql
 * @date 2024/01/09
 */
@Slf4j
public class ThreadPoolFactory {
    /**
     * 线程池长期维持的线程数: 获取可用的处理器核心数
     */
    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    /**
     * 线程数的上限
     */
    private static final int MAX_POOL_SIZE = CORE_POOL_SIZE * 2;

    /**
     * 超时时间，超过时间后多余线程会被收回
     * ms为单位
     */
    private static final long TTL = 1000 * 60 * 5;

    /**
     * 任务的排队队列大小
     */
    private static final int QUEUE_SIZE = 1024;

    /**
     * 用于处理线程中未捕捉的异常处理器
     */
    private static final Thread.UncaughtExceptionHandler UNCAUGHT_EXCEPTION_HANDLER = (t,e)->
        log.warn("Exception happen from thread: {}",t.getName(),e);

    /**
     * 定时任务线程池
     */
    private static ScheduledExecutorService scheduledExecutorService = getScheduledExecutorService();

    /**
     * 线程池
     */
    private static ThreadPoolExecutor threadPoolExecutor = getInstance();

    /**
     * 普通线程Executor
     * @return ThreadPoolExecutor对象
     */
    private static ThreadPoolExecutor getInstance(){
        return new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                TTL,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(QUEUE_SIZE),
                new RaftThread());
    }

    /**
     * 获取定时任务线程服务
     * @return ScheduledExecutorService对象
     */
    private static ScheduledExecutorService getScheduledExecutorService(){
        return new ScheduledThreadPoolExecutor(CORE_POOL_SIZE,new RaftThread());
    }

    /**
     * raft线程
     * @author lql
     * @date 2024/01/09
     */
    static class RaftThread implements ThreadFactory{
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r,"raft-thread");
            t.setUncaughtExceptionHandler(UNCAUGHT_EXCEPTION_HANDLER);
            t.setDaemon(true);
            // 设置线程的优先级
            t.setPriority(5);
            return t;
        }
    }
}
