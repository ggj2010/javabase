package com.ggj.java.rpc.demo.alibaba;

/**
 * @author gaoguangjin
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Hello " + name;
    }
}
