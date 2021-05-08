package com.ggj.java.rpc.demo.spring.firstdemo;

import com.ggj.java.rpc.demo.spring.firstdemo.rpcserver.Server;
import com.ggj.java.rpc.demo.spring.firstdemo.rpcserver.config.ProviderConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * 抽象父类实现接口，然后实现接口的部分方法，再定义一些抽象方法放到子类去实现
 * 灵活使用继承+实现
 * @author gaoguangjin
 */
@Slf4j
public abstract class AbstractServer implements Server {

    public abstract void doStart();

    public abstract void doStop();


    @Override
    public <T> void addService(ProviderConfig<T> providerConfig) {

    }

    @Override
    public <T> void removeService(ProviderConfig<T> providerConfig) {

    }

    @Override
    public void start() {
        //执行通用逻辑 例如打日志
        log.info("server start");
        doStart();
    }

    @Override
    public void stop() {
        //执行通用逻辑 例如打日志
        log.info("server end");
        doStop();
    }
}
