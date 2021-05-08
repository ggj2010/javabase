package com.ggj.java.rpc.demo.spring.firstdemo.rpcserver.http;

import com.ggj.java.rpc.demo.spring.firstdemo.AbstractServer;
import com.ggj.java.rpc.demo.spring.firstdemo.common.domain.Disposable;

/**
 * @author gaoguangjin
 */
public class HttpServer extends AbstractServer implements Disposable {
    @Override
    public void doStart() {

    }

    @Override
    public void doStop() {

    }

    @Override
    public void destory() throws Exception {

    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public String getRegistryUrl() {
        return null;
    }
}
