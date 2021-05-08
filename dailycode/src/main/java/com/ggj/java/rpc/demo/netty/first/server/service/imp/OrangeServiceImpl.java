package com.ggj.java.rpc.demo.netty.first.server.service.imp;


import com.ggj.java.rpc.demo.netty.first.server.annation.RpcService;
import com.ggj.java.rpc.demo.netty.first.server.service.OrangeService;

/**
 * @author gaoguangjin
 */
@RpcService
public class OrangeServiceImpl implements OrangeService {
    @Override
    public String getOrangePrice(Integer size) {
        return "orange cost:"+size*2;
    }
}
