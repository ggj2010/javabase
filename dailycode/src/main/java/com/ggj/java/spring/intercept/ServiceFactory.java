package com.ggj.java.spring.intercept;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gaoguangjin
 */
public class ServiceFactory {
    private static ConcurrentHashMap<String, Object> cacheService = new ConcurrentHashMap<>();


    public static Object getProxy(String remoteClassName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Object object = cacheService.get(remoteClassName);
        if (object == null) {
            synchronized (cacheService) {
                object = cacheService.get(remoteClassName);
                if (object == null) {
                    Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(remoteClassName);
                    object = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), clazz.getInterfaces(), new ProxyInvocation(remoteClassName));
                    cacheService.put(remoteClassName, object);

                }
            }
        }
        return object;
    }
}
