package com.ggj.java.rpc.demo.netty.first.client;

import com.ggj.java.rpc.demo.netty.first.server.service.AppleService;

import java.lang.reflect.Proxy;

/**
 * @author gaoguangjin
 */
public class AppleServiceTest {

    public static void main(String[] args) {
        AppleService appleService= (AppleService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class<?>[]{AppleService.class} ,new RpcInvocationHandler());
        String reuslt = appleService.getApplePrice(12);
        System.out.println("args = [" + reuslt + "]");
    }
}
