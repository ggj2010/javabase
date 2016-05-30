package com.ggj.encrypt.common.utils.mybatis;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多数据源
 * @author:gaoguangjin
 * @date 2016/5/30 14:34
 */
public class MyAbstractRoutingDataSource extends AbstractRoutingDataSource {
    private AtomicInteger count = new AtomicInteger(0) ;
    private final int dataSourceNumber;

    public MyAbstractRoutingDataSource(int dataSourceNumber){
        this.dataSourceNumber=dataSourceNumber;
    }
    @Override
    protected Object determineCurrentLookupKey() {
        String typeKey=DataSourceContextHolder.getJdbcType();
        if(typeKey.equals(DataSourceType.write)) return  DataSourceType.read;
        //读 简单负载均衡
        int number=count.getAndAdd(1);
        int lookupKey = number % dataSourceNumber;
        return new Integer(lookupKey);
    }

    public static void main(String[] args) {
        System.out.println("args = [" + DataSourceType.read + "]");
    }
}
