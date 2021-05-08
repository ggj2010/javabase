package threadpool.dynamics;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author gaoguangjin
 */
@Slf4j
public class TreadPoolMonitor {
    public static void main(String[] args) {
        //无界队列
        ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(4, 8, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(900), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                log.error("reject");
            }
        });


        for (int i = 0; i <1000 ; i++) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        while (true) {
            /**
             * 线程池需要执行的任务数
             */
            long taskCount = threadPoolExecutor.getTaskCount();
            log.info("总任务数：{}",taskCount);
            /**
             * 线程池在运行过程中已完成的任务数
             */
            long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
            log.info("线程池在运行过程中已完成的任务数：{}",completedTaskCount);
            /**
             * 曾经创建过的最大线程数
             */
            long largestPoolSize = threadPoolExecutor.getLargestPoolSize();
            log.info("曾经创建过的最大线程数：{}",largestPoolSize);
            /**
             * 线程池里的线程数量
             */
            long poolSize = threadPoolExecutor.getPoolSize();
            log.info("当前线程数：{}",poolSize);
            /**
             * 线程池里活跃的线程数量
             */
            long activeCount = threadPoolExecutor.getActiveCount();
            log.info("活跃线程数：{}",activeCount);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
       // new Fixed
    }
}
