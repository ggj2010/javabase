package com.ggj.java.rpc.demo.spring.firstdemo.rpcserver.Listener;

import com.ggj.java.rpc.demo.spring.firstdemo.rpcserver.ProviderBootStrap;
import com.ggj.java.rpc.demo.spring.firstdemo.rpcserver.ServiceFactory;

/**
 * @author gaoguangjin
 */
public class ShutDownHookListener implements Runnable {
    @Override
    public void run() {

        try{
            //停止发布所有服务
            ServiceFactory.unPublishAllService();
        }catch (Exception e){
            //...
        }
        try{
            //
            ProviderBootStrap.shutDown();
        }catch (Exception e){
            //...
        }
    }
}
