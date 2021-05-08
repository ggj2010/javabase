package com.ggj.java.spring.intercept;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

/**
 * 模拟远程调用
 * @author gaoguangjin
 */
@Slf4j
@Service
public class RegisterIntercept implements BeanPostProcessor {
    private final static String scannPackage = "com.ggj.java.spring.intercept";

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (skip(bean)) {
            return bean;
        }
        log.info("beanName={}", beanName);
        Class<?> beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();
        if (fields == null) {
            return bean;
        }
        try {
            for (Field field : fields) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                RemoteClass remoteClass = field.getAnnotation(RemoteClass.class);
                if (remoteClass == null) {
                    continue;
                }
                String remoteClassName = remoteClass.className();
                Object proxyClass = ServiceFactory.getProxy(remoteClassName);
                if (proxyClass != null) {
                    field.set(bean, proxyClass);
                }
            }
        } catch (Exception e) {
            log.error("failed to init remote service reference at field", e);
        }
        return bean;
    }

    private boolean skip(Object bean) {
        String clazzName = bean.getClass().getName();
        if (!clazzName.startsWith(scannPackage)) {
            return true;
        }
        return false;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (skip(bean)) {

        }
        return bean;
    }
}
