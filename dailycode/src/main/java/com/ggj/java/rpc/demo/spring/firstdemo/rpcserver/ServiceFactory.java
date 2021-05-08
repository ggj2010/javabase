package com.ggj.java.rpc.demo.spring.firstdemo.rpcserver;

import com.ggj.java.rpc.demo.spring.firstdemo.rpcserver.config.ProviderConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * service工厂
 * @author gaoguangjin
 */
@Slf4j
public class ServiceFactory {


    static {
        try {
            ProviderBootStrap.init();
        }catch (Throwable e){
            log.error("error while initializing service factory:", e);
            System.exit(1);
        }
    }

    public static void addService(ProviderConfig providerConfig) {
        try {
            checkServiceName(providerConfig);
            ServicePublisher.addService(providerConfig);
            ServicePublisher.publicService(providerConfig);
        } catch (Exception e) {
            throw new RuntimeException("addService error:" + providerConfig, e);
        }
    }

    /**
     * check当前url是否已经被公司占用了
     * @param providerConfig
     */
    private static void checkServiceName(ProviderConfig providerConfig) {
        //...
        if(StringUtils.isEmpty(providerConfig.getUrl())){
            providerConfig.setUrl(providerConfig.getInterfaceClass().getName());
        }
    }

    /**
     * 取消发布所有service
     */
    public static void unPublishAllService() {
        //todo
    }
}
