package com.ggj.java.curator.sample;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;


/**
 * @author:gaoguangjin
 * @date 2016/9/1 18:20
 */
@Slf4j
public class demo1 {
    public static final String zookeeperConnectionString = "localhost:2180,localhost:2181,localhost:2182";

    public static void main(String[] args) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
        client.start();
        client .usingNamespace("demo2");
        try {
           // create(client,"/demo1test","demo1test");
//            create(client,"/demotest/1","1");
//            create(client,CreateMode.EPHEMERAL,"/demotest/2","2");
//            create(client,"/demotest/3","3");
            display(client,"/demotest");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void display(CuratorFramework client, String rootPath) throws Exception {
        List<String> list = client.getChildren().forPath(rootPath);
        for (String s : list) {
            log.info("打印节点："+s +" <==>节点值："+new String(client.getData().forPath(rootPath+"/"+s),"utf-8"));
        }
    }


    public static void create(CuratorFramework client, CreateMode model, String path, String data) throws Exception {
       if(!checkExists(client,path)){
           client.create().creatingParentsIfNeeded().withMode(model).forPath(path,data.getBytes());
       }
    }

    public static void create(CuratorFramework client, String path, String data) throws Exception {
       if(!checkExists(client,path)){
           client.create().creatingParentsIfNeeded().forPath(path,data.getBytes());
       }
    }

    public static boolean checkExists(CuratorFramework client,String path) throws Exception {
        return client.checkExists().forPath(path) == null ? false : true;
    }
}
