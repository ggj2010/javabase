package com.ggj.db.sharding.quickstart;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.jdbc.ShardingDataSource;
import com.ggj.db.sharding.common.CommonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * sharding-jdbc：是在客户端进行水平切分操作分库分表。
 *
 * @author:gaoguangjin
 * @date 2017/4/6 9:57
 */
@Slf4j
public class QuickStart {
	
	public static void main(String[] args) {
		// qucik使用 两个数据源ds_0和ds_1 然后表用了t_order和t_order_item
		qucikStart();
	}
	
	/**
	 * 数据源配置：分库需要的
	 * 表配置：分表需要的
	 * 分库策略和分表策略
	 */
	private static void qucikStart() {
		try {
			// 创建数据源
			ShardingDataSource shardingDataSource = getShardingDataSource();
			CommonUtil.crud(shardingDataSource);
		} catch (Exception e) {
			log.error("" + e.getLocalizedMessage());
		}
	}
	
	/**
	 * JDBC认为对于分片策略存有两种维度
	 * - 数据源分片策略DatabaseShardingStrategy：数据被分配的目标数据源
	 * - 表分片策略TableShardingStrategy：数据被分配的目标表，该目标表存在与该数据的目标数据源内。
	 * 故表分片策略是依赖与数据源分片策略的结果的 这里注意的是两种策略的API完全相同，以下针对策略API的讲解将适用于这两种策略
	 *
	 * @return
	 */
	private static ShardingDataSource getShardingDataSource() {
		// 创建两个数据源ds_0和ds_1
		Map<String, DataSource> dataSourceMap = new HashMap<>(2);
		//ds_0 是用来判断哪个表的
		dataSourceMap.put("ds_0", CommonUtil.createDataSource("ds_0"));
		dataSourceMap.put("ds_1", CommonUtil.createDataSource("ds_1"));
		DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap);
		return CommonUtil.getShardingDataSource(dataSourceRule);
	}
}
