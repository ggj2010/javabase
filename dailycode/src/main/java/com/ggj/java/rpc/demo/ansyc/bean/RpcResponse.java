package com.ggj.java.rpc.demo.ansyc.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gaoguangjin
 */
@Data
public class RpcResponse implements Serializable {
    private String requestId;
    private Object result;
}
