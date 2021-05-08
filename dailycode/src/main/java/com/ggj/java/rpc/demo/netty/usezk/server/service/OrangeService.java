package com.ggj.java.rpc.demo.netty.usezk.server.service;

/**
 * @author gaoguangjin
 */
public interface OrangeService {
    /**
     * 获取价格
     * @param size
     * @return
     */
    String getOrangePrice(Integer size);
}
