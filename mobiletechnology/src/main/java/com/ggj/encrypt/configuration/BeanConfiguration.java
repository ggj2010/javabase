package com.ggj.encrypt.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author:gaoguangjin
 * @date 2016/5/23 14:40
 */
@Configuration
@EnableConfigurationProperties(SystemConfiguration.class)
public class BeanConfiguration {

    @Value("${thread.corePoolSize}")
    private String corePoolSize;
    @Value("${thread.maxPoolSize}")
    private String maxPoolSize;
    @Value("${thread.queueCapacity}")
    private String queueCapacity;
    @Value("${thread.keepAliveSeconds}")
    private String keepAliveSeconds;

    /**
     * ThreadPoolTaskExecutor 相比jdk自带Executor  好处是可以统一管理代码里面的线程总数量。
     * @return
     */
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(Integer.parseInt(corePoolSize));
        threadPoolTaskExecutor.setKeepAliveSeconds(Integer.parseInt(keepAliveSeconds));
        threadPoolTaskExecutor.setMaxPoolSize(Integer.parseInt(maxPoolSize));
        threadPoolTaskExecutor.setQueueCapacity(Integer.parseInt(queueCapacity));
        return threadPoolTaskExecutor;
    }
}
