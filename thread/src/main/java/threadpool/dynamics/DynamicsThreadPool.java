package threadpool.dynamics;

import lombok.extern.slf4j.Slf4j;
import threadpool.ExecutorReview;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gaoguangjin
 */
@Slf4j
public class DynamicsThreadPool {


    private static AtomicInteger atomicInteger = new AtomicInteger();

    public static void main(String[] args) {
        int corePoolSize = 4;
        int maxPoolSize = 5;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 10, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(20), new ExecutorReview.NamedThreadFactory("dynamic", true), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                log.error("reject thread {}", atomicInteger.getAndIncrement());
            }
        });

        for (int i = 0; i < 25; i++) {
            threadPoolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        log.info("do nothing CorePoolSize={}", threadPoolExecutor.getCorePoolSize());
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }


        for (int i = 0; i < 20; i++) {
            //动态扩展核心和最大线程数
            if (i == 5) {
                threadPoolExecutor.setMaximumPoolSize(15);
                log.info("扩容");
            }
            threadPoolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        log.info("do nothing CorePoolSize={}", threadPoolExecutor.getCorePoolSize());
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        threadPoolExecutor.shutdown();
    }
}
