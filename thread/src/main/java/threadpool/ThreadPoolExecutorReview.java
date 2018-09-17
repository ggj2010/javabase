package threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author:gaoguangjin
 * @date:2018/9/3
 */
@Slf4j
public class ThreadPoolExecutorReview {
    /**
     * corePoolSize：核心线程数，线程池保持的最低线程数，即使里面的线程闲置。
     maximumPoolSize：最大线程数，线程池允许存在的最大线程数。
     keepAliveTime：非核心线程的闲置存活时间。
     workQueue：阻塞队列，用来存储还未执行的任务。
     threadFactory：线程工厂，用来创建线程池里的新的线程。
     handler：拒绝处理策略，当线程数达到线程池的上限并且阻塞队列满了的时候执行。


     分为3步：
     1. 如果线程池中运行的线程数小于核心线程数，尝试创建一个线程并把当前提交的任务作为他的第一个任务。
     当中的方法addWorker会校验线程池的状态跟有效的线程数量。
     2. 如果一个任务成功加入了阻塞队列，仍需要再次校验，校验失败了将可能回滚或者重新创建一个线程。
     3. 如果加入队列失败了，将尝试创建一个线程，如果还是失败了，将执行拒绝策略。
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("args = [" + args + "]");
        int corePoolSize=1;
        int maximumPoolSize=1;
        long keepAliveTime=1;
        TimeUnit unit=TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue=new ArrayBlockingQueue<Runnable>(1);
        ThreadFactory threadFactory= new ThreadFactory() {
            AtomicInteger atomicInteger=new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                ThreadGroup currentThreadGroup = Thread.currentThread().getThreadGroup();
                return new Thread(currentThreadGroup,r,"pool"+atomicInteger.getAndIncrement()+"-thread-");
            }
        };
        RejectedExecutionHandler handler=new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
               log.error("reject:"+executor.getActiveCount());
            }
        };
        ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue,threadFactory,handler);

        for (int i = 0; i <1 ; i++) {
            threadPoolExecutor.execute(new Thread(){
                @Override
                public void run() {
                    log.info("=====");
                }
            });
        }
        threadPoolExecutor.shutdown();
        //threadPoolExecutor.allowCoreThreadTimeOut(true);


    }
}
