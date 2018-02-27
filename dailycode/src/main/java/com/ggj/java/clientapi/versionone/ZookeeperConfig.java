package com.ggj.java.clientapi.versionone;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * zookeeper信息
 */
@Slf4j
public class ZookeeperConfig {

    public final static String ZK_CONFIG_FILE_PATH = "/data/webapps/zookeeper.properties";
    private static String zkServer;

    static {
        try {
            //1、先加载本地的配置文件信息
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(ZK_CONFIG_FILE_PATH)));
            //如果本地为空，那就加载远程的
            zkServer = properties.getProperty("zkServer");
        } catch (IOException e) {
            log.error("", e);
        } finally {
            if (zkServer == null) {
                log.error("zkServer url is empty");
                System.exit(0);
            }
        }
    }

    public String getZkServer() {
        return zkServer;
    }
}
