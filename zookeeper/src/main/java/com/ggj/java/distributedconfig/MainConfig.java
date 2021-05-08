package com.ggj.java.distributedconfig;


import com.ggj.java.curator.CuratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

/**
 *
 */
@Slf4j
public class MainConfig {
    public static void main(String[] args) throws Exception {
        try {
            CuratorFramework client = CuratorUtil.getClient();
            init(client);
            put(client);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private static void put(CuratorFramework client) {
        //ConfigUtil.putString()；
    }

    private static void init(CuratorFramework client) throws Exception {
        if (!CuratorUtil.checkExists(client, Constant.CONFIG_ROOT_PATH))
            client.create().creatingParentsIfNeeded().forPath(Constant.CONFIG_ROOT_PATH, "config".getBytes());
    }
}
