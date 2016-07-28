package com.ggj.java.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author:gaoguangjin
 * @date 2016/7/27 15:33
 */
public class CuratorUtil {
    public static final String zookeeperConnectionString = "localhost:2180,localhost:2181,localhost:2182";
    /**
     * 初始化获取 CuratorFramework 正式项目里面应该单例
     *
     * @return
     */
    public static CuratorFramework getClient() {
        //ExponentialBackoffRetry基于"backoff"方式重连, 重连时间和重连次数
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
        //The client must be started (and closed when no longer needed).
        client.start();
        return client;
    }
}
