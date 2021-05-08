package com.ggj.java.rpc.demo.ansyc.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gaoguangjin
 */
@Data
public class RpcRequest implements Serializable {
    private String requestId;
    private String methodName;
    private  Class<?>[] getParameterTypes;
    private Object[] args;
    private boolean ansyc;
    private boolean hasReturn;
}
