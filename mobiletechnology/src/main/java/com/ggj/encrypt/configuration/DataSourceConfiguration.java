package com.ggj.encrypt.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 解析配置项
 * @author:gaoguangjin
 * @date 2016/5/30 15:57
 */
@Configuration
@Slf4j
public class DataSourceConfiguration {
	
	@Value("${datasource.type}")
	private Class<? extends DataSource> dataSourceType;
	
	@Bean(name = "writeDataSource")
	@Primary
	@ConfigurationProperties(prefix = "datasource.write")
	public DataSource writeDataSource() {
		log.info("-------------------- writeDataSource init ---------------------");
		return DataSourceBuilder.create().type(dataSourceType).build();
	}
	
	/**
	 * 有多少个从库就要配置多少个
	 * @return
	 */
	@Bean(name = "readDataSource1")
	@ConfigurationProperties(prefix = "datasource.read1")
	public DataSource readDataSourceOne() {
		log.info("-------------------- readDataSourceOne init ---------------------");
		return DataSourceBuilder.create().type(dataSourceType).build();
	}
	
	@Bean(name = "readDataSource2")
	@ConfigurationProperties(prefix = "datasource.read2")
	public DataSource readDataSourceTwo() {
		log.info("-------------------- readDataSourceTwo init ---------------------");
		return DataSourceBuilder.create().type(dataSourceType).build();
	}
}
