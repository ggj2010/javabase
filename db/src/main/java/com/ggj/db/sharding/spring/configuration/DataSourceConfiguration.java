package com.ggj.db.sharding.spring.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.dangdang.ddframe.rdb.sharding.api.MasterSlaveDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.BindingTableRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.jdbc.ShardingDataSource;
import com.ggj.db.sharding.common.CommonUtil;
import com.ggj.db.sharding.common.ModuloDatabaseShardingAlgorithm;
import com.ggj.db.sharding.common.ModuloTableShardingAlgorithm;

import lombok.extern.slf4j.Slf4j;

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
	
	@Bean(name = "shardingDataSource")
	@Primary
	public DataSource ShardingDataSource() {
		log.info("-------------------- ShardingDataSource init ---------------------");
		// 表规则可以指定每张表在数据源中的分布情况
		TableRule orderTableRule = TableRule.builder("t_order").actualTables(Arrays.asList("t_order_0", "t_order_1"))
				.dataSourceRule(dataSourceRule()).build();
		TableRule orderItemTableRule = TableRule.builder("t_order_item")
				.actualTables(Arrays.asList("t_order_item_0", "t_order_item_1")).dataSourceRule(dataSourceRule())
				.build();
		ShardingRule shardingRule = ShardingRule.builder().dataSourceRule(dataSourceRule())
				.tableRules(Arrays.asList(orderTableRule, orderItemTableRule))
				// 绑定表代表一组表，这组表的逻辑表与实际表之间的映射关系是相同的。比如t_order与t_order_item就是这样一组绑定表关系,它们的分库与分表策略是完全相同的,那么可以使用它们的表规则将它们配置成绑定表
				.bindingTableRules(Collections
						.singletonList(new BindingTableRule(Arrays.asList(orderTableRule, orderItemTableRule))))
				.databaseShardingStrategy(
						new DatabaseShardingStrategy("user_id", new ModuloDatabaseShardingAlgorithm()))
				.tableShardingStrategy(new TableShardingStrategy("order_id", new ModuloTableShardingAlgorithm()))
				// 多分片键值
				// .tableShardingStrategy(new TableShardingStrategy(Arrays.asList("order_id","user_id"), new
				// MultipleKeysModuloTableShardingAlgorithm()))
				.build();
		return new ShardingDataSource(shardingRule);
	}
	
	@Bean(name = "dataSourceRule")
	public DataSourceRule dataSourceRule() {
		// 创建两个数据源ds_0和ds_1
		Map<String, DataSource> dataSourceMap = new HashMap<>();
		// master、salve、salve......
		// 数据源1 一个主库、2个从库
		dataSourceMap.put("ds_0", MasterSlaveDataSourceFactory.createDataSource("这个name可以随便定义aa",
				CommonUtil.createDataSource("ds_0_master"), CommonUtil.createDataSource("ds_0_slave_0")));
		// 数据源2 一个主库、2个从库
		dataSourceMap.put("ds_1", MasterSlaveDataSourceFactory.createDataSource("这个name可以随便定义bb",
				CommonUtil.createDataSource("ds_1_master"), CommonUtil.createDataSource("ds_1_slave_0")));
		DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap);
		return dataSourceRule;
	}
	
	@Bean(name = "ds_0_master")
	@ConfigurationProperties(prefix = "datasource.ds_0_master")
	public DataSource masteOneDataSource() {
		log.info("-------------------- ds_0_master init ---------------------");
		return DataSourceBuilder.create().type(dataSourceType).build();
	}
	
	@Bean(name = "ds_0_slave_0")
	@ConfigurationProperties(prefix = "datasource.ds_0_slave_0")
	public DataSource slaveOneDataSourceOne() {
		log.info("-------------------- ds_0_slave_0 init ---------------------");
		return DataSourceBuilder.create().type(dataSourceType).build();
	}
	
	@Bean(name = "ds_1_master")
	@ConfigurationProperties(prefix = "datasource.ds_1_master")
	public DataSource masteTwoDataSource() {
		log.info("-------------------- ds_1_master init ---------------------");
		return DataSourceBuilder.create().type(dataSourceType).build();
	}
	
	@Bean(name = "ds_1_slave_0")
	@ConfigurationProperties(prefix = "datasource.ds_1_slave_0")
	public DataSource slaveTwoDataSourceOne() {
		log.info("-------------------- ds_1_slave_0 init ---------------------");
		return DataSourceBuilder.create().type(dataSourceType).build();
	}
}
