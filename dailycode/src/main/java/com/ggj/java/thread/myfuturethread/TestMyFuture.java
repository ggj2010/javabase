package com.ggj.java.thread.myfuturethread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author:gaoguangjin
 * @date:2018/9/3
 */
@Slf4j
public class TestMyFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureThread<String> futureThread=new FutureThread(new Callable() {
            @Override
            public String call() throws Exception {
                Thread.sleep(5000);
                return "1234";
            }
        });
        Thread thread=new Thread(futureThread);
        thread.start();
        doOtherTask();
        //中断线程
        //thread.interrupt();
        String result = futureThread.get();
        log.info("result {}", result);
    }
    private static void doOtherTask() {
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("do other task");
        }
    }
}
