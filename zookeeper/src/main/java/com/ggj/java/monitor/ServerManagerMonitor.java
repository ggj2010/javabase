package com.ggj.java.monitor;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

import java.util.List;

/**
 * author:gaoguangjin
 * Description:比如有A/B两台服务器提供服务，如果某一台挂了,监控中心需要收到通知
 * 类似于dubbo 服务端启动时候会往zookeeper进行注册，显示所有服务端提供的服务接口信息等等
 * Email:335424093@qq.com
 * Date 2016/2/16 10:17
 */
public class ServerManagerMonitor {

    public static void main(String[] args) {
       monitor();
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void monitor() {
        ZkClient zc=new ZkClient("localhost:2181",1000);
        //创建监控节点
        if(!zc.exists("/monitor"))
        zc.create("/monitor",null, CreateMode.PERSISTENT);

        if(!zc.exists("/monitor/client"))
        zc.create("/monitor/client",null, CreateMode.PERSISTENT);

        zc.subscribeChildChanges("/monitor/client",new IZkChildListener(){
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println("------------客户端发生变化---------childPath="+parentPath );
                currentChilds.forEach((String childPath)->{
                    System.out.println("parentPath = [" + parentPath + "], currentChilds = [" + currentChilds + "]");
                });
            }
        });



    }
}
