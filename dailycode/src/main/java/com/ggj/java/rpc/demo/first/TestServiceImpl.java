package com.ggj.java.rpc.demo.first;

import lombok.extern.slf4j.Slf4j;

/**
 * @author gaoguangjin
 */
@Slf4j
public class TestServiceImpl implements TestService {
    @Override
    public String testMethod(String name) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return name + "==>callback44";
    }

    @Override
    public void testMethod2(String name) {
        log.info("执行：{}",name);
    }
}
