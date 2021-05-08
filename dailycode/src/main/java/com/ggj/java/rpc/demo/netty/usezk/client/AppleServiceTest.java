package com.ggj.java.rpc.demo.netty.usezk.client;

import com.ggj.java.rpc.demo.netty.usezk.server.service.AppleService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author gaoguangjin
 */
@Slf4j

public class AppleServiceTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        AppleService appleService = (AppleService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{AppleService.class}, new RpcInvocationHandler());

        test(appleService);
    }

    private static void test(AppleService appleService) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, Long.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10), new MyPoolThreadFactory("RPC-TEST"), new MyRejectedExecutionHandler());
        for (int i = 0; i < 1; i++) {
            threadPoolExecutor.submit(new Thread(() -> {
                while (true) {
                    try {
                        String reuslt = appleService.getApplePrice(12);
                        System.out.println("appleService = [" + reuslt + "]");
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        //log.error("", e);
                    }
                }

            }));
        }
    }

    ;

    static class MyRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            log.error("pool rejected task:{}", r.toString());
            throw new RejectedExecutionException("Task " + r.toString() +
                    " rejected from " +
                    r.toString());
        }
    }

}
