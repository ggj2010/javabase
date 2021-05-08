package com.ggj.java.rpc.demo.netty.first.vo;


import java.io.Serializable;

/**
 * rpc Response
 *
 * @author gaoguangjin
 */
public class RpcResponse implements Serializable {
    private static final long serialVersionUID = 4831197102608185929L;
    private String requestId;
    private Object response;
    /**
     * 异常需要返回
     */
    private String excetion;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public String getExcetion() {
        return excetion;
    }

    public void setExcetion(String excetion) {
        this.excetion = excetion;
    }
}
