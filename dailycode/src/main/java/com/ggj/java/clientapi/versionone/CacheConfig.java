package com.ggj.java.clientapi.versionone;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 设计一个分布式配置中心的客户端,基于zookeeper实现的。
 * 1、环境分为test与prd
 * 2、zookeeper的server地址 支持从配置文件/data/webapps/zookeeper.properties
 */
@Slf4j
public class CacheConfig {
    private ConcurrentHashMap<String,CacheConfig>  cacheConfigMap=new ConcurrentHashMap<>();

    public static CacheConfig getInstance(){
        return getInstance();
    }

}
