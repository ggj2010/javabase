package com.ggj.java.monitor;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/2/16 10:17
 */
public class ClientB {
    public static void main(String[] args) {
        ZkClient zc=new ZkClient("localhost:2181",1000);
        //创建监控节点
        zc.create("/monitor/client/clientB","clientB", CreateMode.EPHEMERAL);
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
