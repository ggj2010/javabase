package com.ggj.java.rpc.demo.spring.firstdemo.rpcserver;

import com.ggj.java.rpc.demo.spring.firstdemo.register.RegisterManager;
import com.ggj.java.rpc.demo.spring.firstdemo.rpcserver.config.ProviderConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gaoguangjin
 */
@Slf4j
public class ServicePublisher {
    private static ConcurrentHashMap<String, ProviderConfig<?>> serviceCache = new ConcurrentHashMap<String, ProviderConfig<?>>();

    /**
     * group+version 如何实现
     *
     * @param providerConfig
     */
    public static void addService(ProviderConfig providerConfig) {
        String version = providerConfig.getVersion();
        String url = providerConfig.getUrl();
        if (StringUtils.isEmpty(version)) {
            serviceCache.put(url, providerConfig);
        } else {
            String urlWithVersion = String.format(url + "_%s", version);
            serviceCache.put(urlWithVersion, providerConfig);
        }
    }

    /**
     * 发布服务
     *
     * @param providerConfig
     */
    public static void publicService(ProviderConfig providerConfig) {
        log.info("publish service:{}", providerConfig.getUrl());
        String serviceUrl = providerConfig.getUrl();
        Optional<String> existOptional = serviceCache.keySet().stream().filter(s -> s.equals(serviceUrl)).findAny();
        log.info("try to publish service to registry:{}", providerConfig, "existing service:{}", existOptional.isPresent());
        List<Server> servers = ProviderBootStrap.getServers();
        for (Server server : servers) {
            publishServiceToRegistry(serviceUrl, server.getRegistryUrl(), server.getPort());
        }
    }

    private synchronized static <T> void publishServiceToRegistry(String serviceUrl, String registryUrl, int port) {
        String serverAddress = registryUrl + ":" + port;
        try {
            RegisterManager.getInstance().getRegister().registerService(serviceUrl, serverAddress);
        } catch (Exception e) {
            log.error("register servier error", e);
        }
    }
}
