package com.ggj.java.curator.lock;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

import com.ggj.java.curator.CuratorUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 读写锁和可重入锁
 * http://ifeve.com/zookeeper-lock/
 * @author:gaoguangjin
 * @date 2016/7/28 14:42
 */
@Slf4j
public class ZookeeperLock {

    private static final String PATH = "/root/lock";


    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorUtil.getClient();
        //启动main方法两次
//        readWriteLock(client);
        interProcessMutex(client);
    }

    private static void interProcessMutex(CuratorFramework client) throws Exception {
        InterProcessMutex interProcessMutex = new InterProcessMutex(client, PATH);
        if (interProcessMutex.acquire(2, TimeUnit.SECONDS)) {
            doTask("interProcessMutex");
        } else {
            log.info("获取不到interProcessMutex锁");
        }
    }

    private static void readWriteLock(CuratorFramework client) {
        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(client, PATH);
        InterProcessLock readLock = lock.readLock();
        InterProcessLock writeLock = lock.writeLock();
        try {
            if (writeLock.acquire(100, TimeUnit.SECONDS)) {
                doTask("writeLock");
            } else {
                log.info("获取不到写锁");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writeLock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            if (readLock.acquire(100, TimeUnit.SECONDS)) {
                doTask("readLock");
            } else {
                log.info("获取不到读锁");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                readLock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void doTask(String name) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            log.info(name + "执行task ");
            Thread.sleep(5000);
        }
    }
}
