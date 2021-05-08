package com.ggj.java.distributedconfig;

import org.apache.commons.lang3.StringUtils;

/**
 * 抽象类
 * 检验放到这一层
 */
public abstract class AbstractConfigManager implements ConfigManager {

    abstract String doGet(String key, String defualtValue);

    @Override
    public String get(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new NullPointerException("key is null");
        }
        return doGet(key, null);
    }
    @Override
    public String get(String key, String defaultValue) {
        return null;
    }
}
