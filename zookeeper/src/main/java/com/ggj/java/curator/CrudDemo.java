package com.ggj.java.curator;

import java.util.Collection;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorTransaction;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import lombok.extern.slf4j.Slf4j;

/**
 * curator api 学习
 * http://curator.apache.org/getting-started.html
 *
 * @author:gaoguangjin
 * @date 2016/7/26 14:12
 */
@Slf4j
public class CrudDemo {
    public static final String zookeeperConnectionString = "localhost:2180,localhost:2181,localhost:2182";

    public static void main(String[] args) {
        CuratorFramework client = getClient();
          crud(client);
        //事物 增删改可以带事物
//        transation(client);
    }

    /**
     * Curator支持事务，一组crud操作同生同灭
     * @param client
     */
    private static void transation(CuratorFramework client) {
        try {
            // 开启事务
            CuratorTransaction transaction = client.inTransaction();
            Collection<CuratorTransactionResult> results = transaction.create()
                    .forPath("/root/transation", "transation".getBytes()).and().create()
                    .forPath("/root/transation2", "transation2".getBytes()).and()
                    .delete().forPath("/root/transation").and()
                    .delete().forPath("/root/transation2").and().commit();
            for (CuratorTransactionResult result : results) {
                System.out.println(result.getForPath() + " - " + result.getType());
            }
        }catch (Exception e){
            log.error("transation exception ", e);
        }
    }

    /**
     * withMode(CreateMode.EPHEMERAL) 可以设置节点的类型
     * 如果主节点是临时的话，就不能构建其子节点 NoChildrenForEphemeralsException
     * 临时节点在会话结束30s会删除
     * @param client
     */
    private static void crud(CuratorFramework client) {
        try {
            String rootPath = "/root";
            String parentPath = "/root/parent";
            String parentPath2 = "/root/parent2";
            String childPath = "/root/parent/child";
            //增删改
            delete(client, childPath,parentPath,parentPath2,rootPath);
            client.create().withMode(CreateMode.PERSISTENT).forPath(rootPath, "root".getBytes());
            client.create().withMode(CreateMode.PERSISTENT).forPath(parentPath, "parent".getBytes());
            client.create().withMode(CreateMode.PERSISTENT).forPath(parentPath2, "parent2".getBytes());
            client.setData().forPath(parentPath2, "parent2update".getBytes());
            client.create().withMode(CreateMode.PERSISTENT).forPath(childPath, "child1".getBytes());
            List<String> list = client.getChildren().forPath(rootPath);
            display(list,client,rootPath);
        } catch (Exception e) {
            log.error("crud exception ", e);
        }
    }

    private static void delete(CuratorFramework client, String childPath, String parentPath, String parentPath2, String rootPath) throws Exception {
        boolean rootExists = client.getZookeeperClient().getZooKeeper().exists(rootPath, false) == null ? false : true;
        boolean parentExists = client.getZookeeperClient().getZooKeeper().exists(parentPath, false) == null ? false : true;
        boolean parent2Exists = client.getZookeeperClient().getZooKeeper().exists(parentPath2, false) == null ? false : true;
        boolean childExists = client.getZookeeperClient().getZooKeeper().exists(childPath, false) == null ? false : true;
        if (childExists) client.delete().forPath(childPath);
        if (parentExists) client.delete().forPath(parentPath);
        if (parent2Exists) client.delete().forPath(parentPath2);
       // if (rootExists) client.delete().forPath(rootPath);
    }

    private static void display(List<String> list, CuratorFramework client, String rootPath) throws Exception {
        for (String s : list) {
            log.info("打印节点："+s +" <==>节点值："+new String(client.getData().forPath(rootPath+"/"+s),"utf-8"));
        }
    }

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
