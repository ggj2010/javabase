package com.ggj.java.rpc.demo.netty.usezk.server.service;

/**
 * @author gaoguangjin
 */
public interface AppleService {

    /**
     * 苹果价格
     * @param size
     * @return
     */
    String getApplePrice(Integer size);
}
