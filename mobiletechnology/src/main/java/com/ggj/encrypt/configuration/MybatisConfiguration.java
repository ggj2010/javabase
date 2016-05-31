package com.ggj.encrypt.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.ggj.encrypt.common.utils.SpringContextHolder;
import com.ggj.encrypt.common.utils.mybatis.DataSourceType;
import com.ggj.encrypt.common.utils.mybatis.MyAbstractRoutingDataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * 重写SqlSessionFactory
 * @author:gaoguangjin
 * @date 2016/5/30 11:18
 */
@Configuration
@AutoConfigureAfter({ DataSourceConfiguration.class })
@Slf4j
public class MybatisConfiguration extends MybatisAutoConfiguration {
	@Value("${datasource.readSize}")
	private String dataSourceSize;
	
	@Bean
	public SqlSessionFactory sqlSessionFactorys() throws Exception {
		log.info("-------------------- 重载父类 sqlSessionFactory init ---------------------");
		return super.sqlSessionFactory(roundRobinDataSouceProxy());
	}

	/**
	 * 有多少个数据源就要配置多少个bean
	 * @return
	 */
	@Bean(name = "roundRobinDataSouceProxy")
	public AbstractRoutingDataSource roundRobinDataSouceProxy() {
		int size = Integer.parseInt(dataSourceSize);
		MyAbstractRoutingDataSource proxy = new MyAbstractRoutingDataSource(size);
		Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
		DataSource writeDataSource = SpringContextHolder.getBean("writeDataSource");
		// 写
		targetDataSources.put(DataSourceType.write.getType(), SpringContextHolder.getBean("writeDataSource"));
		
		for (int i = 0; i < size; i++) {
			targetDataSources.put(i, SpringContextHolder.getBean("readDataSource" + (i + 1)));
		}
		proxy.setDefaultTargetDataSource(writeDataSource);
		proxy.setTargetDataSources(targetDataSources);
		return proxy;
	}
	
}
