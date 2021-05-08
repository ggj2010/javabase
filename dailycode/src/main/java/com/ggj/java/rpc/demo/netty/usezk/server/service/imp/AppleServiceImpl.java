package com.ggj.java.rpc.demo.netty.usezk.server.service.imp;


import com.ggj.java.rpc.demo.netty.usezk.server.annation.RpcService;
import com.ggj.java.rpc.demo.netty.usezk.server.service.AppleService;

/**
 * @author gaoguangjin
 */
@RpcService
public class AppleServiceImpl implements AppleService {
    @Override
    public String getApplePrice(Integer size) {
        return "apple cost:"+size*5;
    }
}
