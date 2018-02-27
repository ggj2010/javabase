package com.ggj.java.thread;


import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author:gaoguangjin
 * @date:2018/1/25
 */
public class ThreadLocalMemoryLeackTest {
    private static ThreadLocal<Byte[]> threadLocal = new ThreadLocal<>();


    public static void main(String[] args) {
        ThreadLocalMemoryLeackTest threadLocalMemoryLeackTest = new ThreadLocalMemoryLeackTest();
        // threadLocalMemoryLeackTest.test();
        //threadLocalMemoryLeackTest.test2();
    }

    private void test2() {
        for (int i = 0; i <= 150; i++) {
            getThread2().start();
        }
    }

    private void test() {
        ExecutorService pool = Executors.newFixedThreadPool(150);
        for (int i = 0; i <= 10000 * 10000; i++) {
            pool.execute(getThread());
            System.out.println(i + "");
        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Runnable getThread() {
        return new Thread() {
            @Override
            public void run() {
                threadLocal.set(new Byte[1024 * 1024 * 1]);
                System.out.println("2222");
            }
        };
    }

    public Thread getThread2() {
        return new Thread() {
            @Override
            public void run() {
                while (true) {
                    threadLocal.set(new Byte[1 * 1024 * 1024]);
                    System.out.println(Thread.currentThread().getName());

                }
            }
        };
    }
}
