package com.ggj.java.rpc.demo.spring.firstdemo.testcase.server.service;

import com.ggj.java.rpc.demo.spring.firstdemo.testcase.server.pojo.TestBean;

/**
 * @author gaoguangjin
 */
public interface TestRPCService {

    String getNowTime(String name);
    String save(TestBean testBean);
}
