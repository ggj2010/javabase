package threadpool.pool;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程统一管理，所有的线程
 * 所有定时的线程以future的形式执行
 * future 最好设置超时时间，放置堵塞线程
 * @author:gaoguangjin
 * @date 2017/1/9 15:53
 */
public class ThreadManage {

    private static final String DEFAULT_THREAD_POOL_KEY = "default-thread-pool";
    private BlockingQueue<KeyFuture<?>> futuresBlockQueue = new LinkedBlockingQueue<KeyFuture<?>>();
    //定义多个线程池，每个线程池执行不同的线程
    private ConcurrentHashMap<String, ExecutorService> executorServiceMap = new ConcurrentHashMap<>();
    private ThreadPoolExecutor defaultThreadPoolExecutor;

    private ThreadManage() {
        //int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,BlockingQueue<Runnable> workQueue
        defaultThreadPoolExecutor = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(), new NamedThreadFactory(DEFAULT_THREAD_POOL_KEY, false));
        //下面两种写法一致
        //ExecutorService executorService = Executors.newFixedThreadPool(10);
        //ExecutorService executorService2 =new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        defaultThreadPoolExecutor.execute(observeTask());

        executorServiceMap.put(DEFAULT_THREAD_POOL_KEY,defaultThreadPoolExecutor);
    }

    private Runnable observeTask() {

        return  new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        KeyFuture<?> future = futuresBlockQueue.take();
                        ExecutorService pool = executorServiceMap.get(future.getKey());
                        


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }
        };
    }

    public ThreadManage getThreadManage() {
        return SingleThreadManage.threadManage;
    }

    private static class SingleThreadManage {

        private static final ThreadManage threadManage = new ThreadManage();
    }


    private static class NamedThreadFactory implements ThreadFactory {

        private static final AtomicInteger poolNumber = new AtomicInteger(1);

        private final ThreadGroup group;

        private final AtomicInteger threadNumber = new AtomicInteger(1);

        private final String namePrefix;

        private boolean mDaemo = false;

        NamedThreadFactory(String name, boolean mDaemo) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = name + "pool-" + poolNumber.getAndIncrement() + "-thread-";
            this.mDaemo = mDaemo;
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            t.setDaemon(mDaemo);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
