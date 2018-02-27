package com.ggj.java.spring.overrideparam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Service;

/**
 * @author:gaoguangjin
 * @date:2017/10/31
 */
@Service
@Slf4j
public class PropertiestService implements BeanFactoryPostProcessor {
    @Value("${lion.test.url}")
    private String url;

    public String test() {
        return url;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        String[] beanNames = configurableListableBeanFactory.getBeanDefinitionNames();
        log.info("BeanFactoryPostProcessor 2");
    }
}
