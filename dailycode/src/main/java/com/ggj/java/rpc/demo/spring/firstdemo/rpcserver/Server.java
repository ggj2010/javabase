package com.ggj.java.rpc.demo.spring.firstdemo.rpcserver;

import com.ggj.java.rpc.demo.spring.firstdemo.rpcserver.config.ProviderConfig;

/**
 * @author gaoguangjin
 */
public interface Server {
    public <T> void addService(ProviderConfig<T> providerConfig);
    public <T> void removeService(ProviderConfig<T> providerConfig);
    public void start();
    public void stop();
    public boolean isStarted();
    public int getPort();
    public String getRegistryUrl();
}
