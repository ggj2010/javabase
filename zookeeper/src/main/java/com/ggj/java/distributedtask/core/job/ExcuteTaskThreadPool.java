package com.ggj.java.distributedtask.core.job;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gaoguangjin
 */
@Slf4j
public class ExcuteTaskThreadPool {
    //
    public static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<>(), new InnerThreadFactory("jobPool"), new MyRejectedExecutionHandler());

    private static class InnerThreadFactory implements ThreadFactory {
        private String poolName;
        private AtomicInteger atomicInteger = new AtomicInteger(1);

        public InnerThreadFactory(String poolName) {
            this.poolName = poolName;
        }

        @Override
        public Thread newThread(Runnable r) {
            ThreadGroup currentThreadGroup = Thread.currentThread().getThreadGroup();
            return new Thread(currentThreadGroup, r, poolName + atomicInteger.getAndIncrement() + "-thread-");
        }
    }

    private static class MyRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            log.error("excute thread error", r);
        }
    }

    public static ThreadPoolExecutor getThreadPoolExecutor(){
        return threadPoolExecutor;
    }

}
