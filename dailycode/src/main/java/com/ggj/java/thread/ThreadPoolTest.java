
package com.ggj.java.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 方法，导致线程池里面的5个线程的threadLocals变量里面的new LocalVariable()实例没有被释放，虽然线程池里面的任务执行完毕了，但是线程池里面的5个线程会一直存在直到JVM退出。这里需要注意的是由于localVariable被声明了static，虽然线程的ThreadLocalMap里面是对localVariable的弱引用，localVariable也不会被回收。运行结果二的代码由于线程在设置localVariable变量后即使调用了localVariable.remove()方法进行了清理，所以不会存在内存泄露。
 * @author:gaoguangjin
 * @date:2018/8/13
 */
public class ThreadPoolTest {

    static class LocalVariable {
        private Long[] a = new Long[1024 * 1024];
    }

    // (1)
    final static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>());
    // (2)
    final static ThreadLocal<LocalVariable> localVariable = new ThreadLocal<LocalVariable>();

    public static void main(String[] args) throws InterruptedException {
        // (3)
        for (int i = 0; i < 50; ++i) {
            poolExecutor.execute(new Runnable() {
                public void run() {
                    // (4)
                    localVariable.set(new LocalVariable());
                    // (5)
                    System.out.println("use local varaible");
                     localVariable.remove();

                }
            });

            Thread.sleep(1000);
        }
        // (6)
        System.out.println("pool execute over");
    }
}
