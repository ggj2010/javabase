package com.ggj.java.distributeconfig;

/**
 *
 */
public interface ConfigManager {
    String get(String key);

    String get(String key, String defaultValue);
}
