package com.ggj.java.rpc.demo.spring.firstdemo.testcase.server.pojo;

import java.io.Serializable;

/**
 * @author gaoguangjin
 */
public class TestBean implements Serializable {
    private static final long serialVersionUID = -6420658498430145175L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
