package com.ggj.java.rpc.demo.second;

import com.ggj.java.rpc.demo.first.TestService;

import java.io.IOException;

/**
 * @author gaoguangjin
 */
public class TestClientTwo {
    public static void main(String[] args) throws IOException {
        TestService testService = ConsumerClient.getProxyClass(TestService.class);
        for (int i = 0; i < 10; i++) {
            System.out.println(testService.testMethod("clienttwo"+i));
        }
    }
}
