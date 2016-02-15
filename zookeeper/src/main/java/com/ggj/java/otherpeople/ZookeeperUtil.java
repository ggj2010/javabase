package com.ggj.java.otherpeople;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/2/15 15:24
 */
public class ZookeeperUtil implements Watcher {
    private static class PropertiesDynLoading {

        public static final String connectString = "192.168.57.128:9181";
        public static final int sessionTimeout = 3000;
        public static final String authScheme = "digest";
        public static final String accessKey = "cache:svcctlg";
        public static final boolean authentication = false;
    }

    private ZooKeeper zk;

    /**
     * 创建zookeeper客户端
     *
     * @return
     */
    private boolean createZkClient() {
        try {
            zk = new ZooKeeper(PropertiesDynLoading.connectString, PropertiesDynLoading.sessionTimeout, this);
        } catch (IOException e) {
            this.log("{}", e);
            e.printStackTrace();
            return false;
        }
        if (PropertiesDynLoading.authentication) {
            zk.addAuthInfo(PropertiesDynLoading.authScheme, PropertiesDynLoading.accessKey.getBytes());
        }
        if (!isConnected()) {
            log(" ZooKeeper client state [{}]", zk.getState().toString());
        }
        try {
            if (zk.exists("/zookeeper", false) != null) {
                log("create ZooKeeper Client Success! connectString", PropertiesDynLoading.connectString);
                log(" ZooKeeper client state [{}]", zk.getState());
                return true;
            }
        } catch (Exception e) {
            this.log("create ZooKeeper Client Fail! connectString", PropertiesDynLoading.connectString);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 新增持久化节点
     *
     * @param path
     *            节点路径
     * @param data
     *            节点数据
     * @return
     */
    private boolean createPersistentNode(String path, String data) {
        if (isConnected()) {

            try {
                if (PropertiesDynLoading.authentication) {
                    zk.create(path, data.getBytes(), getAdminAcls(), CreateMode.PERSISTENT);
                } else {
                    zk.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                log("{}", e);
                return false;
            }

        }
        this.log("zookeeper state", zk.getState());
        return false;
    }

    /**
     * 创建瞬时节点
     *
     * @param path
     * @param data
     * @return
     */
    private boolean creatEphemeralNode(String path, String data) {
        if (isConnected()) {

            try {
                if (PropertiesDynLoading.authentication) {
                    zk.create(path, data.getBytes(), getAdminAcls(), CreateMode.PERSISTENT);
                } else {
                    zk.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                log("{}", e);
                return false;
            }

        }
        this.log("zookeeper state", zk.getState());
        return false;
    }

    /**
     * 修改数据
     *
     * @param path
     * @param data
     * @return
     */
    private boolean setNodeData(String path, String data) {
        if (isConnected()) {
            try {
                zk.setData(path, data.getBytes(), -1);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                this.log("{}", e);
                return false;
            }
        }
        this.log("zookeeper state = [{}]", zk.getState());
        return false;
    }

    /**
     * 删除节点
     *
     * @param path
     * @return
     */
    private boolean deleteNode(String path) {
        if (isConnected()) {
            try {
                zk.delete(path, -1);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                this.log("{}", e);
                return false;
            }
        }
        this.log("zookeeper state = [{}]", zk.getState());
        return false;
    }

    /**
     * 获取节点值
     *
     * @param path
     * @return
     */
    public String getNodeData(String path) {
        if (isConnected()) {
            String data = null;
            try {
                byte[] byteData = zk.getData(path, true, null);
                data = new String(byteData, "utf-8");
                return data;
            } catch (Exception e) {

                e.printStackTrace();
                this.log("{}", e);
                return null;
            }
        }
        this.log("zookeeper state = [{}]", zk.getState());
        return null;
    }

    /**
     * 获取path子节点名列表
     *
     * @param path
     * @return
     */
    public List<String> getChildren(String path) {
        if (isConnected()) {
            String data = null;
            try {
                return zk.getChildren(path, false);
            } catch (Exception e) {
                e.printStackTrace();
                this.log("{}", e);
                return null;
            }
        }
        this.log("zookeeper state = [{}]", zk.getState());
        return null;
    }

    public boolean startZkClient() {
        return createZkClient();
    }

    /**
     * zookeeper是否连接服务器
     *
     * @return
     */
    public boolean isConnected() {
        return zk.getState().isConnected();
    }

    /**
     * 是否存在path路径节点
     *
     * @param path
     * @return
     */
    public boolean exists(String path) {
        try {
            return zk.exists(path, false) != null;
        } catch (Exception e) {

            this.log("{}", e);
        }
        return false;
    }

    /**
     * 关闭zookeeper
     */
    public void closeZk() {
        if (isConnected()) {
            try {
                zk.close();
                this.log("close zookeeper [{}]", "success");
            } catch (InterruptedException e) {
                this.log("zookeeper state = [{}]", e);
                e.printStackTrace();
            }
        } else {
            this.log("zookeeper state = [{}]", zk.getState());
        }

    }

    /**
     *
     * @return
     */
    public List<ACL> getCreateNodeAcls() {
        List<ACL> listAcls = new ArrayList<ACL>(3);
        try {
            Id id = new Id(PropertiesDynLoading.authScheme,
                    DigestAuthenticationProvider.generateDigest(PropertiesDynLoading.accessKey));
            ACL acl = new ACL(Perms.CREATE, id);
            listAcls.add(acl);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
            return Ids.OPEN_ACL_UNSAFE;
        }
        return listAcls;
    }

    public List<ACL> getAdminAcls() {
        List<ACL> listAcls = new ArrayList<ACL>(3);
        try {
            Id id = new Id(PropertiesDynLoading.authScheme,
                    DigestAuthenticationProvider.generateDigest(PropertiesDynLoading.accessKey));
            ACL acl = new ACL(Perms.ALL, id);
            listAcls.add(acl);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
            return Ids.OPEN_ACL_UNSAFE;
        }
        return listAcls;
    }

    public void log(String format, Object args) {
        int index = format.indexOf("{");
        StringBuilder sb = new StringBuilder(format);
        sb.insert(index + 1, "%s");
        System.out.println(String.format(sb.toString(), args));
    }

    @Override
    public void process(WatchedEvent event) {
    }

    public static void main(String[] args) {

        ZookeeperUtil zkc = new ZookeeperUtil();
        zkc.createZkClient();

        if (!zkc.exists("/windowcreate")) {

            zkc.createPersistentNode("/windowcreate", "windowcreate");
        }
        if (!zkc.exists("/windowcreate/value")) {
            System.out.println("not exists /windowcreate/value");

            zkc.createPersistentNode("/windowcreate/value", "A0431P001");
        }
        if (!zkc.exists("/windowcreate/valuetmp")) {
            System.out.println("not exists /windowcreate/valuetmp");
            zkc.creatEphemeralNode("/windowcreate/valuetmp", "A0431P002");
        }
        System.out.println(zkc.getNodeData("/zookeeper"));
        System.out.println(zkc.getChildren("/windowcreate"));
        System.out.println(zkc.getNodeData("/windowcreate/value"));
        System.out.println(zkc.getNodeData("/windowcreate/valuetmp"));
        zkc.setNodeData("/windowcreate/value", "A0431P003");
        System.out.println(zkc.getNodeData("/windowcreate/value"));
        zkc.deleteNode("/windowcreate/value");
        System.out.println(zkc.exists("/windowcreate/value"));
        zkc.closeZk();
    }

}
