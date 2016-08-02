package com.ggj.encrypt.common.utils.mybatis;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

/**
 * 多数据源切换
 * @author:gaoguangjin
 * @date 2016/5/30 14:34
 */
@Slf4j
public class MyAbstractRoutingDataSource extends AbstractRoutingDataSource {
	private final int dataSourceNumber;
	private AtomicInteger count = new AtomicInteger(0);
	
	public MyAbstractRoutingDataSource(int dataSourceNumber) {
		this.dataSourceNumber = dataSourceNumber;
	}
	
	@Override
	protected Object determineCurrentLookupKey() {
		Object resultObject=null;
		String typeKey = DataSourceContextHolder.getJdbcType();
		//只对主库开启事务，如果typeKey为空表示获取主库的datasource
		if (StringUtils.isEmpty(typeKey)){
			resultObject= DataSourceType.write.getType();
		}else{
			// 读简单负载均衡
			int number = count.getAndAdd(1);
			int lookupKey = number % dataSourceNumber;
			resultObject= new Integer(lookupKey);
		}
		log.info("determineCurrentLookupKey:"+resultObject);
		return resultObject;
	}
}
