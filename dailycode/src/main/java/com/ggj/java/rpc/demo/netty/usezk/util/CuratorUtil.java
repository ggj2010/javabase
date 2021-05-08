package com.ggj.java.rpc.demo.netty.usezk.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author gaoguangjin
 */
@Slf4j
public class CuratorUtil {

    public static boolean checkExists(CuratorFramework client, String path) throws Exception {
        return client.checkExists().forPath(path) == null ? false : true;
    }

    /**
     * 带模式的创建
     *
     * @param client
     * @param createMode
     * @param path
     * @param data
     * @throws Exception
     */
    public static void create(CuratorFramework client, CreateMode createMode, String path, String data) throws Exception {
        if (!checkExists(client, path)) {
            client.create().creatingParentsIfNeeded().withMode(createMode).forPath(path, data.getBytes());
        }
    }

    public static void update(CuratorFramework client, String path, String data, boolean check) throws Exception {
        if (check) {
            if (checkExists(client, path)) {
                update(client, path, data);
            }
        } else {
            update(client, path, data);
        }
    }

    public static void update(CuratorFramework client, String path, String data) throws Exception {
        if (checkExists(client, path)) {
            client.setData().forPath(path, data.getBytes());
        }
    }

    /**
     * 默认 节点是PERSISTENT
     *
     * @param path
     * @param data
     * @throws Exception
     */
    public static void create(CuratorFramework client, String path, String data, boolean check) throws Exception {
        if (check) {
            if (!checkExists(client, path)) {
                create(client, path, data);
            }
        } else {
            create(client, path, data);
        }
    }

    /**
     * 不会主动check
     *
     * @param client
     * @param path
     * @param data
     * @throws Exception
     */
    public static void create(CuratorFramework client, String path, String data) throws Exception {
        client.create().creatingParentsIfNeeded().forPath(path, data.getBytes());
    }


    public static byte[] createOrGet(CuratorFramework client, String path, String data) throws Exception {
        if (!checkExists(client, path)) {
            client.create().creatingParentsIfNeeded().forPath(path, data.getBytes());
            return null;
        } else {
            return client.getData().forPath(path);
        }
    }

    /**
     * 获取指定path的值
     *
     * @param client
     * @param path
     * @return
     * @throws Exception
     */
    public static byte[] getData(CuratorFramework client, String path,boolean watch) throws Exception {
        if(watch){
            return client.getData().watched().forPath(path);
        }
        return client.getData().forPath(path);
    }

    public static String getStringDataAndWatch(CuratorFramework client, String path) throws Exception {
        byte[] bytes = getData(client, path,true);
        if (bytes != null) {
            return new String(bytes, "utf-8");
        } else {
            return null;
        }
    }

    public static String getStringData(CuratorFramework client, String path) throws Exception {
        byte[] bytes =getData(client,path,false);
        if (bytes != null) {
            return new String(bytes, "utf-8");
        } else {
            return null;
        }
    }

    private static String getString(byte[] bytes) throws UnsupportedEncodingException {
        if (bytes == null) {
            return null;
        }
        return new String(bytes, "utf-8");
    }

    public static Integer getInteger(byte[] bytes) throws UnsupportedEncodingException {
        int num = 0;
        String value = getString(bytes);
        try {
            num = Integer.parseInt(value);
        } catch (Exception e) {
            log.error("", e);
        }
        return num;
    }

    public static List<String> getChildPath(CuratorFramework client, String path) throws Exception {
        if (!checkExists(client, path)) {
            return null;
        }
        List<String> childPathList = client.getChildren().forPath(path);
        if (CollectionUtils.isEmpty(childPathList)) {
            return null;
        }
        return childPathList;
    }

    public static Map<Integer, String> getMapChildValue(CuratorFramework client, String path) throws Exception {
        Map<Integer, String> childValueMap = new TreeMap<>();
        List<String> list = client.getChildren().forPath(path);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        for (String childPath : list) {
            String childFullPath = String.format(path + "/%s", childPath);
            String value = getString(client.getData().forPath(childFullPath));
            childValueMap.put(value == null ? 0 : Integer.parseInt(value), childFullPath);
        }
        return childValueMap;
    }


    public static void deleteNode(CuratorFramework client, String path) throws Exception {
        if (checkExists(client, path)) {
            client.delete().forPath(path);
        }
    }
}
