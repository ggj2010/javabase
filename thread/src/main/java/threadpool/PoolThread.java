package threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/12 14:14
 */
@Slf4j
public class PoolThread {

    public static void main(String[] args) {
//        Java通过Executors提供四种线程池，分别为：
//        newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程，线程如果60s没有使用就会移除出Cache。
//        newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
//        newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。
//        newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。

        PoolThread pt = new PoolThread();
//        pt.cachedThreadPool();
        pt.fixedThread();
//        pt.singleThread();
//        pt.schelThread();
//        pt.await();

    }

    /**
     * 新的线程会复用以前的线程。
     * 2016-01-12 14:39:09.785 [pool-1-thread-1] INFO  threadpool.PoolThread 50 run- 线程名称：pool-1-thread-1
     * 2016-01-12 14:39:09.785 [pool-1-thread-2] INFO  threadpool.PoolThread 50 run- 线程名称：pool-1-thread-2
     * 2016-01-12 14:39:09.785 [pool-1-thread-6] INFO  threadpool.PoolThread 50 run- 线程名称：pool-1-thread-6
     * 2016-01-12 14:39:09.785 [pool-1-thread-4] INFO  threadpool.PoolThread 50 run- 线程名称：pool-1-thread-4
     * 2016-01-12 14:39:09.785 [pool-1-thread-5] INFO  threadpool.PoolThread 50 run- 线程名称：pool-1-thread-5
     * 2016-01-12 14:39:09.785 [pool-1-thread-3] INFO  threadpool.PoolThread 50 run- 线程名称：pool-1-thread-3
     * 2016-01-12 14:39:12.783 [pool-1-thread-3] INFO  threadpool.PoolThread 50 run- 线程名称：pool-1-thread-3
     */
    public void cachedThreadPool() {
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            pool.execute(getThread(i + ""));
        }
        pool.execute(getThread("6"));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //虽然我们定义了是7但是还是复用了现有的线程
        pool.execute(getThread("7"));
        pool.shutdown();
    }

    /**
     * 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。输出结果是一开始先输出两个 再输出两个
     * 2016-01-12 15:56:36.748 [pool-1-thread-1] INFO  threadpool.PoolThread 77 run- 线程名称：pool-1-thread-1
     * 2016-01-12 15:56:36.748 [pool-1-thread-2] INFO  threadpool.PoolThread 77 run- 线程名称：pool-1-thread-2
     * 2016-01-12 15:56:36.752 [pool-1-thread-1] INFO  threadpool.PoolThread 77 run- 线程名称：pool-1-thread-1
     * 2016-01-12 15:56:36.752 [pool-1-thread-2] INFO  threadpool.PoolThread 77 run- 线程名称：pool-1-thread-2
     * 2016-01-12 15:56:36.753 [pool-1-thread-1] INFO  threadpool.PoolThread 77 run- 线程名称：pool-1-thread-1
     */
    public void fixedThread() {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 5; i++) {
            pool.execute(getSleepThread(i + ""));
        }
        pool.shutdown();
    }

    /**
     * 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行 先进先执行
     * 2016-01-12 16:02:40.672 [pool-1-thread-1] INFO  threadpool.PoolThread 109 run- 线程名称：pool-1-thread-1==name:0
     * 2016-01-12 16:02:40.677 [pool-1-thread-1] INFO  threadpool.PoolThread 109 run- 线程名称：pool-1-thread-1==name:1
     * 2016-01-12 16:02:40.677 [pool-1-thread-1] INFO  threadpool.PoolThread 109 run- 线程名称：pool-1-thread-1==name:2
     * 2016-01-12 16:02:40.677 [pool-1-thread-1] INFO  threadpool.PoolThread 109 run- 线程名称：pool-1-thread-1==name:3
     * 2016-01-12 16:02:40.677 [pool-1-thread-1] INFO  threadpool.PoolThread 109 run- 线程名称：pool-1-thread-1==name:4
     */
    public void singleThread() {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 5; i++) {
            pool.execute(getSingleThread(i + ""));
        }
        pool.shutdown();
    }

    /**
     * 创建一个定长线程池，支持定时及周期性任务执行
     */
    public void schelThread() {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        pool.scheduleAtFixedRate(new Runnable() {// 每隔一段时间就触发异常
            public void run() {
                log.info("scheduled 线程池：1"+Thread.currentThread().getName());
            }
        }, 0, 5000, TimeUnit.MILLISECONDS);

        pool.scheduleAtFixedRate(new Runnable() {// 每隔一段时间打印系统时间，证明两者是互不影响的
            public void run() {
                log.info("scheduled 线程池：2"+Thread.currentThread().getName());
            }
        }, 0, 2000, TimeUnit.MILLISECONDS);

        pool.scheduleWithFixedDelay(new Runnable() {// scheduleWithFixedDelay与scheduleAtFixedRate区别在于，scheduleWithFixedDelay会等待当前线程执行完了，再延迟
            public void run() {
                log.info("scheduled 线程池：3"+Thread.currentThread().getName());
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 2000, TimeUnit.MILLISECONDS);
    }

    /**
     * 等待线程池所有线程都执行完 执行下面的
     */
    public void await() {
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            pool.execute(getThread(i + ""));
        }
        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);// 用于等待子线程结束，再继续执行下面的代码。该例中我设置一直等着子线程结束。输出时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("所有线程执行结束了噢！！！！");
    }


    public Thread getThread(final String name) {
        return new Thread(name) {
            public void run() {
                log.info("线程名称：" + Thread.currentThread().getName());
            }
        };
    }

    public Thread getSingleThread(final String name) {
        return new Thread(name) {
            public void run() {
                log.info("线程名称：" + Thread.currentThread().getName() + "==name:" + name);
            }
        };
    }

    /**
     * 用来演示newFixedThreadPool 可以控制线程的数量
     *
     * @param name
     * @return
     */
    public Thread getSleepThread(final String name) {
        return new Thread(name) {
            public void run() {
                log.info("线程名称：" + Thread.currentThread().getName());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

}
