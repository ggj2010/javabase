package com.ggj.java.spring.intercept;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author gaoguangjin
 */
@Slf4j
public class ProxyInvocation implements InvocationHandler {
    private String remoteClassName;

    public ProxyInvocation(String remoteClassName) {
        this.remoteClassName=remoteClassName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("模拟get remote的class");
        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(remoteClassName);
        Object remoteObject = clazz.newInstance();
        return method.invoke(remoteObject,args);
    }
}
