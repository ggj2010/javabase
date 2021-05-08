package com.ggj.java.rpc.demo.spring.firstdemo.common.extention;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gaoguangjin
 */
public class ExtensionLoader {
    private static Map<Class<?>, Object> extensionCache = new ConcurrentHashMap<>();
    private static Map<Class<?>, List<?>> listExtensionCache = new ConcurrentHashMap<>();

    public static <T> T newExtension(Class<T> clazz) {
        T extensionObject = (T) extensionCache.get(clazz);
        if (extensionObject == null) {
            ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);
            for (T t : serviceLoader) {
                extensionCache.put(clazz, t);
                return t;
            }
        }
        return extensionObject;
    }

    public static <T> List<T> newExtensionList(Class<T> clazz) {
        List<T> extensionListObject = (List<T>) listExtensionCache.get(clazz);
        if (CollectionUtils.isEmpty(extensionListObject)) {
            ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);
            List<T> extensionList = new ArrayList<T>();
            for (T t : serviceLoader) {
                extensionList.add(t);
            }
            if (!CollectionUtils.isEmpty(extensionList)) {
                listExtensionCache.put(clazz, extensionList);
            }
        }
        return extensionListObject;
    }

    public static <T> List<T> getExtensionList(Class<T> clazz) {
        List<T> extensions = (List<T>) listExtensionCache.get(clazz);
        if (extensions == null) {
            extensions = newExtensionList(clazz);
            if (!extensions.isEmpty()) {
                listExtensionCache.put(clazz, extensions);
            }
        }
        return extensions;
    }
}
