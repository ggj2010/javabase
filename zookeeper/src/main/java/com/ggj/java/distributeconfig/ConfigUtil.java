package com.ggj.java.distributeconfig;

import com.ggj.java.curator.CuratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;

/**
 *
 */
@Slf4j
public class ConfigUtil {
    private static CuratorFramework client = null;

    private ConfigUtil() {
    }

    /**
     * 业务名称+"."+key
     * education-admin-web.serverName
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(String key, String defaultValue) throws Exception {
        try {
            if (StringUtils.isEmpty(key)) {
                return defaultValue;
            }
            String[] array = key.split(".");
            if (array == null || array.length < 2) {
                return defaultValue;
            } else {
                String path = String.format("%s/%s", Constant.CONFIG_ROOT_PATH, key.replace(".", "/"));
                byte[] byteData = getClient().getData().forPath(path);
                return new String(byteData);
            }
        } catch (Exception e) {
            log.error("", e);
            return defaultValue;
        }
    }

    private static CuratorFramework getClient() {
        if (client == null) {
            synchronized (ConfigUtil.class) {
                if (client == null) {
                    client = CuratorUtil.getClient();
                }
            }
        }
        return client;
    }


    public static Boolean putString(String key, String value) throws Exception {
        try {
            if (StringUtils.isEmpty(key)) {
                return false;
            }
            String[] array = key.split(".");
            if (array == null || array.length < 2) {
                return false;
            } else {
                String path = String.format("%s/%s", Constant.CONFIG_ROOT_PATH, key.replace(".", "/"));
                if (!CuratorUtil.checkExists(client, path)) {
                    client.create().forPath(path, value.getBytes());
                }
                return true;
            }
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }
}
