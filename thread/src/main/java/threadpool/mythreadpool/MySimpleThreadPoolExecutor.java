
package threadpool.mythreadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自定义线程池
 * 核心线程
 * 最大线程
 * 线程超时释放线程
 *
 * @author:gaoguangjin
 * @date:2018/9/7
 */
@Slf4j
public class MySimpleThreadPoolExecutor implements Executor {
    private volatile int corePoolSize;
    private volatile int maxPoolSize;
    private  int keepAliveTime;
    private volatile int currentPoolSize;
    private RejectHandler rejectHandler;
    private BlockingQueue<Runnable> taskQueue;

    private Set<MyWorker> workerSet = new HashSet<>();

    private ThreadPoolFactory threadPoolFactory = new ThreadPoolFactory();

    private Lock lock = new ReentrantLock();

    interface RejectHandler {
        public void reject(Runnable runnable,
                MySimpleThreadPoolExecutor mySimpleThreadPoolExecutor);
    }

    class ThreadPoolFactory {
        private AtomicInteger thredCount = new AtomicInteger();

        public Thread newThread(Runnable runnable) {
            ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
            String threadName = "pool-" + thredCount.getAndIncrement() + "-thread";
            return new Thread(threadGroup, runnable, threadName);
        }
    }

    class MyWorker implements Runnable {
        private Runnable firstTask;
        private Thread thread;

        public MyWorker(Runnable firstTask) {
            this.firstTask = firstTask;
            this.thread = threadPoolFactory.newThread(this);
        }

        @Override
        public void run() {
            loopRun(this);
        }

        private void loopRun(MyWorker myWorker) {
            Runnable task = myWorker.firstTask;
            while (true) {
                try {
                    if (task != null || ((task = getTask()) != null)) {
                        task.run();
                    } else {
                        break;
                    }
                } finally {
                    task = null;
                }
            }

        }

        public Runnable getTask() {
            Runnable runnable = null;
            try {




                runnable = taskQueue.take();
            } catch (InterruptedException e) {
                return null;
            }
            return null;
        }
    }

    public MySimpleThreadPoolExecutor(int corePoolSize, int maxPoolSize, BlockingQueue taskQueue,
            RejectHandler rejectHandler,int keepAliveTime) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.taskQueue = taskQueue;
        this.rejectHandler = rejectHandler;
        this.keepAliveTime=keepAliveTime;
    }

    @Override
    public void execute(Runnable command) {
        if (null == command) {
            throw new NullPointerException();
        }
        // 如果当前线程数小于核心线程数,则创建新的线程
        if (currentPoolSize < corePoolSize) {
            addWorker(command);
        } else {
            // 队列没有满，则不需要添加新的线程
            if (taskQueue.offer(command)) {

            } else {
                // 队列满了，且当前线程小于最大线程可以继续创建线程
                if (currentPoolSize < maxPoolSize) {
                    addWorker(command);
                } else {
                    reject(command);
                }
            }
        }
    }

    /**
     * 拒绝
     * 
     * @param command
     */
    private void reject(Runnable command) {
        rejectHandler.reject(command, this);
    }

    private void addWorker(Runnable command) {
        lock.lock();
        try {
            currentPoolSize++;
            MyWorker worker = new MyWorker(command);
            worker.thread.start();
            workerSet.add(worker);
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {

        BlockingQueue blockingQueue1 = new LinkedBlockingQueue();
        BlockingQueue blockingQueue2 = new SynchronousQueue();
        MySimpleThreadPoolExecutor mySimpleThreadPoolExecutor = new MySimpleThreadPoolExecutor(4,
                20, blockingQueue2, new RejectHandler() {
                    @Override
                    public void reject(Runnable runnable,
                            MySimpleThreadPoolExecutor mySimpleThreadPoolExecutor) {
                        log.info("reject:{}", runnable);
                    }
                },10);

        for (int i = 0; i < 100; i++) {
            mySimpleThreadPoolExecutor.execute(new Thread() {
                @Override
                public void run() {
                    log.info("=====");
                }
            });
        }
        //执行 shutdown
        log.info("执行 shutdown");
        mySimpleThreadPoolExecutor.shutdown();

    }

    private void shutdown() {
        for (MyWorker myWorker : workerSet) {
            log.info("shudown==>" + myWorker.thread.getName());
            myWorker.thread.interrupt();
        }
    }
}
