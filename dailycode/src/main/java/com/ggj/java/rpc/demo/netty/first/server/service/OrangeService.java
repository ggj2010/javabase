package com.ggj.java.rpc.demo.netty.first.server.service;

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
