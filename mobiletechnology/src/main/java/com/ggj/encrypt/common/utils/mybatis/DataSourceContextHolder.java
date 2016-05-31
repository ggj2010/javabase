package com.ggj.encrypt.common.utils.mybatis;

/**
 * 本地线程全局变量，用来存放当前操作是读还是写
 * @author:gaoguangjin
 * @date 2016/5/30 14:34
 */
public class DataSourceContextHolder {
	private static final ThreadLocal<String> local = new ThreadLocal<String>();
	
	public static ThreadLocal<String> getLocal() {
		return local;
	}
	
	/**
	 * 读可能是多个库
	 */
	public static void read() {
		local.set(DataSourceType.read.getType());
	}
	
	/**
	 * 写只有一个库
	 */
	public static void write() {
		local.set(DataSourceType.write.getType());
	}
	
	public static String getJdbcType() {
		return local.get();
	}
}
