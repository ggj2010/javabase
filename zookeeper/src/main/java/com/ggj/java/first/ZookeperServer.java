package com.ggj.java.first;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/2/15 12:27
 */
@Slf4j
public class ZookeperServer {

    static ZooKeeper zk = null;

    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = getZookeeper();
        System.in.read();
        zooKeeperAPITest(zooKeeper);
        //授权与验证
       // auth(zooKeeper);

    }

    /**
     * 授权访问
     * @param zooKeeper
     */
    private static  List<ACL> auth(ZooKeeper zooKeeper) throws Exception {
        //  授权与验证
         String auth = "admin:admin";
        zooKeeper.addAuthInfo("digest",auth.getBytes("UTF-8"));
        Id id = new Id("digest", DigestAuthenticationProvider.generateDigest(auth));
        ACL acl = new ACL(ZooDefs.Perms.ALL, id);
        List<ACL> acls = Collections.singletonList(acl);

        //zooKeeper.create("/safe","safe".getBytes(),acls, CreateMode.PERSISTENT);
        return acls;
    }

    /**
     * 有四种类型的znode：
     1、PERSISTENT-持久化目录节点
     客户端与zookeeper断开连接后，该节点依旧存在
     2、 PERSISTENT_SEQUENTIAL-持久化顺序编号目录节点
     客户端与zookeeper断开连接后，该节点依旧存在，只是Zookeeper给该节点名称进行顺序编号
     3、EPHEMERAL-临时目录节点
     客户端与zookeeper断开连接后，该节点被删除
     4、EPHEMERAL_SEQUENTIAL-临时顺序编号目录节点
     客户端与zookeeper断开连接后，该节点被删除，只是Zookeeper给该节点名称进行顺序编号
     * @param zooKeeper
     */
    private static void zooKeeperAPITest(ZooKeeper zooKeeper) {
        try {
            //路径、内容、、模式（PERSISTENT持久）
            zooKeeper.create("/root","中国".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            zooKeeper.create("/root/beijin","北京市".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            zooKeeper.create("/root/anhui","安徽省".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            zooKeeper.create("/root/anhui/hefei","合肥市".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            zooKeeper.create("/root/anhui/wuhu","芜湖市".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            // 取得/root节点下的子节点名称,返回List<String>,false标示不watch
            List<String> listStr = zooKeeper.getChildren("/root", false);
            for (String string : listStr)
                log.info("节点名称有:"+string);

            //修改
            zooKeeper.setData("/root","中国2016".getBytes(),-1);
            byte[] data = zooKeeper.getData("/root", false, null);
            log.info("/root节点修改后的值："+new String(data));

            //  删除创建过的
            deleteAll(zooKeeper,"/root");
        } catch (Exception e) {
            log.error(""+e.getLocalizedMessage());
        }
    }

    /**
     * 递归删除所有节点
     * @param zooKeeper
     * @param path
     * @throws KeeperException
     * @throws InterruptedException
     */
    private static void deleteAll(ZooKeeper zooKeeper,String path) throws KeeperException, InterruptedException {
        List<String> listStr = zooKeeper.getChildren(path, false);
        for (String child : listStr){
            String childPath = path + "/" + child;
            deleteAll(zooKeeper,childPath);
        }
        zooKeeper.delete(path,-1);
    }

    private static ZooKeeper getZookeeper() {
        ZooKeeper zookeeper=null;
        try {
            zookeeper = new ZooKeeper("localhost:2181", 60000, new Watcher() {
                public void process(WatchedEvent watchedEvent) {
                  // log.info(watchedEvent.toString());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zookeeper;
    }



    private static void test() {
        try {
            // 第二个参数为Session超时时间，第三个为节点变化时
            zk = new ZooKeeper("localhost:2181", 60000, new Watcher() {
                // 监控所有被触发的事件
                public void process(WatchedEvent event) {
                }
            });
        } catch (Exception e) {
            log.error("错误！" + e.getLocalizedMessage());
        }
    }


}
