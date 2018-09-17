package com.ggj.java.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author:gaoguangjin
 * @date:2018/2/26
 */
@Slf4j
public class FutureTest {
    public static void main(String[] args) {
        testJDKFutrue();
    }
    private static void testJDKFutrue() {
        FutureTask task = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                try {
                    Thread.sleep(3000);
                    log.info("--执行查询---");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 123213213;
            }
        });

        new Thread(task) {}.start();
        doOtherTask();
        try {
            Object result = task.get();
            log.info("result {}", result);
        } catch (Exception e) {
            log.error("", e);
        }
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
