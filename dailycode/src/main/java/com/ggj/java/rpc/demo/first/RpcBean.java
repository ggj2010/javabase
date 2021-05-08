package com.ggj.java.rpc.demo.first;

import lombok.Data;

/**
 * @author gaoguangjin
 */
@Data
public class RpcBean {
    private String className;
    private String method;
    private String param;

    public RpcBean(String className, String method, String param) {
        this.className = className;
        this.method = method;
        this.param = param;
    }

    public RpcBean() {
    }
}
