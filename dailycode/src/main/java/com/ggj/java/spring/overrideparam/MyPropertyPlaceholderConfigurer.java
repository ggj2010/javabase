package com.ggj.java.spring.overrideparam;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.Random;

@Component
public class MyPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements
        ApplicationContextAware, BeanFactoryAware, BeanNameAware, InitializingBean {

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    }


    @Override
    protected String resolvePlaceholder(String key, Properties props) {
        if (key.startsWith("lion.")) {
            return getRemote(key);
        }
        return super.resolvePlaceholder(key, props);
    }

    private String getRemote(String key) {
        return "从配置中心获取到key=" + key + " 的随机值 " + new Random().nextInt(100);
    }
}
