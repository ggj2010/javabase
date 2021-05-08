package com.ggj.java.distributedconfig;

/**
 *
 */
public interface ConfigManager {
    String get(String key);

    String get(String key, String defaultValue);
}
