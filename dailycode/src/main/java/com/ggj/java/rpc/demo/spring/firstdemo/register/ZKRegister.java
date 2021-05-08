package com.ggj.java.rpc.demo.spring.firstdemo.register;


import com.ggj.java.rpc.demo.spring.firstdemo.register.zookeeper.client.CuratorClient;
import com.ggj.java.rpc.demo.spring.firstdemo.register.zookeeper.client.ZKPathConfig;
import com.ggj.java.rpc.demo.spring.firstdemo.register.zookeeper.client.ZookeeperDataWatcher;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author gaoguangjin
 */
public class ZKRegister implements Register {

    private AtomicBoolean initRegister = new AtomicBoolean();
    private CuratorClient zkClient;

    public ZKRegister() {
        zkClient = new CuratorClient();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getServiceAddress(String serviceName) throws Exception {
        return zkClient.getServiceAddress(serviceName);
    }

    @Override
    public void registerService(String serviceName, String serviceAddress) throws Exception {
        ZKPathConfig zkPathConfig = new ZKPathConfig(serviceName, serviceAddress);
        if (initRegister.compareAndSet(false, true)) {
            initRegisterInfo(zkPathConfig);
        }
        zkClient.registerService(zkPathConfig, serviceAddress);
    }

    /**
     * 注册
     * 机器信息
     * 注册权重
     *
     * @param zkPathConfig
     * @throws Exception
     */
    private void initRegisterInfo(ZKPathConfig zkPathConfig) throws Exception {
        zkClient.registerServerApp(zkPathConfig);
        zkClient.registerWeight(zkPathConfig);
    }

    @Override
    public void unregisterService(String serviceName, String serviceAddress) throws Exception {
        ZKPathConfig zkPathConfig = new ZKPathConfig(serviceName, serviceName);
        zkClient.unregisterService(zkPathConfig, serviceAddress);
    }

    @Override
    public boolean unregisterServerApp(String serviceAddress) {
        return false;
    }

    @Override
    public void initListener(ZookeeperDataWatcher zookeeperDataWatcher) {
        if (zookeeperDataWatcher != null) {
            zkClient.initListener(zookeeperDataWatcher);
        }
    }
}
