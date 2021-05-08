package com.ggj.java.rpc.demo.second;

import com.ggj.java.rpc.demo.first.TestService;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author gaoguangjin
 */
public class TestClientOne {
    public static void main(String[] args) throws IOException {
        TestService testService = ConsumerClient.getProxyClass(TestService.class);
        //预热
        testService.testMethod("clientone");
        int size=1000;
        Long beginTime=System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            testService.testMethod("clientone"+i);
        }
        Long endTime=System.currentTimeMillis();
        //平均qps都在1000以内
        System.out.println(String.format("cost:%s /s",(1000.00/(endTime-beginTime))*1000));

    }
}
