package com.ggj.java.first;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;

/**
 * author:gaoguangjin
 * Description:测试client时候 server需要先启动，watch功能需要先开启
 * Email:335424093@qq.com
 * Date 2016/2/15 12:28
 */
@Slf4j
public class ZookeperClient{
    public static void main(String[] args) {
        ZookeperClient zookeperClient=new ZookeperClient();
        ZooKeeper  zookeeper =zookeperClient.getZookeeper();
        try {
            //先开启wath功能 /root节点
            zookeeper.exists("/root",true);
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private  ZooKeeper getZookeeper() {
        try {
            Watcher wa = new Watcher() {
                // 监控所有被触发的事件
                public void process(WatchedEvent event) {
                    log.info("监控:" + event.getType() + ":" + event.getPath());
                }
            };
            return  new ZooKeeper("localhost:2181",60000,wa);
        } catch (IOException e) {
          log.error(""+e.getLocalizedMessage());
        }
        return null;
    }

}
