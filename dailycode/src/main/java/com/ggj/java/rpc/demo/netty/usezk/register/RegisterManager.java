package com.ggj.java.rpc.demo.netty.usezk.register;


import com.ggj.java.rpc.demo.netty.usezk.register.zookeeper.client.ZookeeperDataWatcher;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 注册中心管理
 *
 * @author gaoguangjin
 */
@Slf4j
public class RegisterManager {
    private static Lock lock = new ReentrantLock();
    private static RegisterManager registerManager = new RegisterManager();
    public Register getRegister() {
        return register;
    }
    private Register register;
    private RegisterManager() {
    }

    /**
     * 服务端调用
     *
     * @return
     */
    public static RegisterManager getInstance() {
        registerManager.init(null);
        return registerManager;
    }

    /**
     * 客户端调用
     *
     * @return
     */
    public static RegisterManager getClientInstance() {
        registerManager.init(new ZookeeperDataWatcher());
        return registerManager;
    }

    /**
     * 初始化,需要加锁防止初始化多次
     *
     * @param zookeeperDataWatcher
     */
    private void init(ZookeeperDataWatcher zookeeperDataWatcher) {
        if (register == null) {
            lock.lock();
            try {
                if (register == null) {
                    register = new ZKRegister();
                    register.initListener(zookeeperDataWatcher);
                }
            } catch (Exception e) {
                log.error("init error", e);
            } finally {
                lock.unlock();
            }
        }
    }
}
