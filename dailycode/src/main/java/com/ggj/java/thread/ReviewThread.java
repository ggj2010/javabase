
package com.ggj.java.thread;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 线程有以下几种状态，NEW，RUNNABLE，BLOCKED，WAITING，TIMED_WAITING，TERMINATED
 *
 * @author:gaoguangjin
 * @date:2018/3/22
 */
@Slf4j
public class ReviewThread extends Thread {
    public ReviewThread(ThreadGroup group, String name) {
        super(group, name);
    }

    public ReviewThread() {
        super();
    }

    public static void main(String[] args) throws IOException {
        ReviewThread reviewThread = new ReviewThread();
        // //线程状态
        // reviewThread.status();
        // //线程优先级
        // priority();
        // //守护线程
        // daemonThread();
        // //线程组
        // threadGroup();
        // //stop
        // threadStop();
        // //suspend
        // threadSuspend();
        // //堵塞
        // System.in.read();
    }

    /**
     * stop会即刻停止run()方法中剩余的全部工作，包括在catch或finally语句中，并抛出ThreadDeath异常
     * 会立即释放该线程所持有的所有的锁，导致数据得不到同步的处理，出现数据不一致的问题。
     */
    private static void threadStop() {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("执行结果：" + i);
            }
        });
        thread.start();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.stop();
    }

    /**
     * 当子线程被挂起的时候如果获取到了System.out锁， 但是主线程打印需要获取到System.out锁， 但是只有子线程被执行了resume才会释放锁，所以造成了死锁。
     */
    private static void threadSuspend() {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                System.out.print("" + i);
            }
        });
        thread.start();
        System.out.println("------");
        thread.suspend();
        System.out.println("线程被挂起");
        for (int i = 0; i < 100; i++) {
            System.out.print("" + i);
        }
        thread.resume();
    }

    /**
     * 可以批量管理线程或线程组对象，有效地对线程或线程组对象进行组织
     */
    private static void threadGroup() {
        ThreadGroup group = new ThreadGroup("testGroup");
        for (int i = 0; i < 2; i++) {
            ReviewThread reviewThread = new ReviewThread(group, "线程组里面线程" + i);
            reviewThread.start();
        }
        while (group.activeCount() > 0) {
            System.out.println("" + group.activeCount());
            group.interrupt();
            break;
        }
    }

    /**
     * 是否为用户线程false和守护线程true，默认是用户线程。守护线程优先级比较低，当主程序执行结束就直接退出。
     */
    private static void daemonThread() {
        Thread thread = new Thread(() -> {
            for (;;) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setName("线程回顾");
        // 是否为用户线程false和守护线程true，默认是用户线程。守护线程优先级比较低，当主程序执行结束就直接退出。
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * 19:31:00.992 [main] INFO com.ggj.java.thread.ReviewThread - NEW 19:31:00.996 [main] INFO
     * com.ggj.java.thread.ReviewThread - RUNNABLE 19:31:00.996 [main] INFO
     * com.ggj.java.thread.ReviewThread - BLOCKED 19:31:01.994 [main] INFO
     * com.ggj.java.thread.ReviewThread - TIMED_WAITING 19:31:03.099 [main] INFO
     * com.ggj.java.thread.ReviewThread - WAITING 19:31:03.099 [main] INFO
     * com.ggj.java.thread.ReviewThread - TERMINATED
     */
    private void status() {
        Thread blockTread = new Thread() {
            @Override
            public void run() {
                synchronizedMethod();
            }
        };
        blockTread.start();

        Thread blockTread2 = new Thread() {
            @Override
            public void run() {
                synchronizedMethod();
                waitWithTimeMethod();
            }
        };
        log.info(blockTread2.getState().name());
        blockTread2.start();
        log.info(blockTread2.getState().name());
        while (true) {
            State state = blockTread2.getState();
            if (state == State.BLOCKED) {
                log.info(state.name());
                break;
            }
        }
        while (true) {
            State state = blockTread2.getState();
            if (state == State.TIMED_WAITING) {
                log.info(state.name());
                notifyMethod();
                break;
            }
        }
        while (true) {
            State state = blockTread2.getState();
            if (state == State.WAITING) {
                log.info(state.name());
                notifyMethod();
                break;
            }
        }
        while (true) {
            State state = blockTread2.getState();
            if (state == State.TERMINATED) {
                log.info(state.name());
                break;
            }
        }
    }

    private synchronized void notifyMethod() {
        notify();
    }

    private synchronized void waitWithTimeMethod() {
        try {
            wait(10);
            waitMethod();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void waitMethod() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程可以设置优先级 线程优先级的大小从1到10，newPriority > MAX_PRIORITY || newPriority <
     * MIN_PRIORITY，优先级越高获得调度的时间可能就越大,但是不一定先执行。 如果超过这个范围就报IllegalArgumentException
     * mian方法的线程优先级是,默认的也是5 线程的优先级具有继承性，比如A线程启动B线程，则A和B的线程优先级是一样的
     * 优先级调度依赖于具体的操作系统调度方式，不能完全依赖于优先级进行线程的资源调度，只有在底层平台不支持线程时，JVM才会自己实现线程的管理和调度,
     * 在Linux上，Java线程的调度最终转化为了操作系统中的进程调度. 总结：不要依赖线程的优先级，如果要设置线程的执行顺序，最好试用加锁方式实现
     */
    private static void priority() {
        ReviewThread thread = new ReviewThread();
        thread.setName("A");
        thread.setPriority(1);
        ReviewThread thread2 = new ReviewThread();
        thread2.setName("B");
        thread2.setPriority(10);
        thread.start();
        thread2.start();
    }

    @Override
    public void run() {
        try {
            sleep(100);
        } catch (InterruptedException e) {
            log.info("线程被interrupted：", e);
        }
        log.info("执行线程：name={},priority={}", Thread.currentThread().getName(),
                Thread.currentThread().getPriority());
    }

    private synchronized void synchronizedMethod() {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义异常捕获类
     */
    static class ThreadException implements UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            log.info("uncaughtException", e);
        }
    }

}
