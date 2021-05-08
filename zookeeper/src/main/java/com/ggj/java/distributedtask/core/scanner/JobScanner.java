package com.ggj.java.distributedtask.core.scanner;

import com.ggj.java.distributedtask.core.annation.DistributeJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * 扫描器
 *
 * @author gaoguangjin
 */
@Slf4j
public class JobScanner {
    private static final String DEFAULT_PACKEG = "com";

    /**
     * 扫指定类
     *
     * @param packageName
     * @return
     */
    public static Map<Class<?>, DistributeJob> scannerWithPath(String packageName) throws Exception {
        Map<Class<?>, DistributeJob> map = new HashMap<>();
        try {
            ResourcePatternResolver rp = new PathMatchingResourcePatternResolver();
            Resource[] resources = rp.getResources(String.format("classpath:%s/*.class", packageName.replaceAll("\\.", "\\/")));
            if (resources == null || resources.length == 0) {
                return null;
            }
            for (Resource resource : resources) {
                String className = resource.getFile().getPath().split("classes\\/")[1].replaceAll("\\/", ".").replaceAll(".class", "");
                Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
                DistributeJob distributeJob = clazz.getAnnotation(DistributeJob.class);
                if (distributeJob != null) {
                    map.put(clazz, distributeJob);
                }
            }
        } catch (Exception e) {
            log.error("scann DistributeJob annation error", e);
            throw e;
        }
        return map;
    }

    /**
     * 扫全部类
     *
     * @return
     */
    public static Map<Class<?>, DistributeJob>  scannerAll() throws Exception {
        return scannerWithPath(DEFAULT_PACKEG);
    }
}
