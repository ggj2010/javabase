package com.ggj.encrypt.common.utils.mybatis;

/**
 * @author:gaoguangjin
 * @date 2016/5/30 14:34
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<String> local=new ThreadLocal<String>();
    public static ThreadLocal<String> getLocal() {
        return local;
    }

    /**
     * 读可能是多个库
     */
    public static void read() {
        local.set(DataSourceType.read.toString());
    }

    /**
     * 写只有一个库
     */
    public static void write() {
        local.set(DataSourceType.write.toString());
    }

    public static String getJdbcType() {
        return local.get();
    }
}
