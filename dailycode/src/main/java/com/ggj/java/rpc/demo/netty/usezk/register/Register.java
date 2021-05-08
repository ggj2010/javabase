package com.ggj.java.rpc.demo.netty.usezk.register;


import com.ggj.java.rpc.demo.netty.usezk.register.zookeeper.client.ZookeeperDataWatcher;

/**
 * @author gaoguangjin
 */
public interface Register {
    String getName();
    String getServiceAddress(String serviceName) throws Exception;
    void registerService(String serviceName, String serviceAddress) throws Exception;
    void unregisterService(String serviceName, String serviceAddress) throws Exception;
    boolean unregisterServerApp(String serviceAddress);
    void initListener(ZookeeperDataWatcher zookeeperDataWatcher);
}
