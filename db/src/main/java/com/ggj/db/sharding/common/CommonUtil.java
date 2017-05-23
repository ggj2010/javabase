package com.ggj.db.sharding.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.dangdang.ddframe.rdb.sharding.api.HintManager;
import com.dangdang.ddframe.rdb.sharding.api.rule.BindingTableRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.jdbc.ShardingDataSource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:gaoguangjin
 * @date 2017/4/7 14:10
 */
@Slf4j
public class CommonUtil {
	
	public static void crud(ShardingDataSource shardingDataSource) throws SQLException{
        delete(shardingDataSource);
        //增删查改
        insert(shardingDataSource);
        select(shardingDataSource);
//        //暗示(Hint)的分片键值
        selectWithHint(shardingDataSource);
    }
	
	public static void display(ResultSet resultSet) throws SQLException {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(resultSet.getInt(1) + ":");
		stringBuilder.append(resultSet.getInt(2) + ":");
		stringBuilder.append(resultSet.getString(3));
		log.info("查询结果集：{}", stringBuilder.toString());
	}
	
	/**
	 * 如果WHERE中没有user_id和order_id的条件，那么是否可以进行分片计算呢？
	 * 可以使用dangdang自带的hitmanager暗示
	 * 如果注释hintManager.addDatabaseShardingValue 那么查询的结果将会从两个数据源返回
	 * 如果hintManager.addTableShardingValue 查询结果将会从两个表返回
	 * 如果都注释返回的是所有数据源和所有表的数据
	 *
	 * @param shardingDataSource
	 */
	private static void selectWithHint(ShardingDataSource shardingDataSource) throws SQLException {
		log.info("查询带有暗示的分片结果集");
		String sql = "SELECT * FROM t_order";
		try (HintManager hintManager = HintManager.getInstance();
				Connection connection = shardingDataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			// 自行添加暗示分片
			// 来添加数据源分片键值
			hintManager.addDatabaseShardingValue("t_order", "user_id", 1);
			// 来添加表分片键值
			hintManager.addTableShardingValue("t_order", "order_id", 1);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while(resultSet.next()) {
					CommonUtil.display(resultSet);
				}
			}
		}
	}
	
	/**
	 * 在编写分片算法的时候，传入的分片键值是来自SQL语句中WHERE条件的。
	 * 例如逻辑表t_order如果其数据源分片键为user_id， 分片算法是奇数值路由到db1偶数值路由到db2；
	 * 表分片键为order_id， 分片算法是奇数值路由到t_order_1偶数值路由到t_order_2，如果执行如下sql语句：
	 * select * from t_order where user_id = 1 and order_id = 2
	 * 那么在数据源分片算法的shardingValue参数将会传入1用于分片计算，结果为路由到db1; 表分片算法的shardingValue参数将会传入2用于分片计算，结果为路由到t_order_2
	 * select * from db1.t_order_2 where user_id = 1 and order_id = 2
	 *
	 * @param shardingDataSource
	 */
	private static void select(ShardingDataSource shardingDataSource) throws SQLException {
		String sql = "SELECT * FROM t_order where order_id=? and user_id=? ";
		try (Connection connection = shardingDataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, 1);
			preparedStatement.setInt(2, 1);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while(resultSet.next()) {
					CommonUtil.display(resultSet);
				}
			}
		}
	}

	/**
	 * try-resource 写法去关闭preparedStatement
	 *
	 * @param shardingDataSource
	 */
	private static void delete(ShardingDataSource shardingDataSource) throws SQLException {
		String sql = "DELETE FROM t_order where order_id=? and user_id=? ";
		try (Connection connection = shardingDataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, 1);
			preparedStatement.setInt(2, 1);
			int number = preparedStatement.executeUpdate();
			log.info("删除{}", number);
		}
	}
	
	/**
	 * 插入
	 * 因为order_id和user_id都是1，根据分库分表规则 1%2
	 * user_id 是用来决定数据库的
	 * order_id是用来决定表的
	 * 插入到了ds_1 和t_order_1的表
	 * 可以到数据库里面自行校验下
	 *
	 * @param shardingDataSource
	 * @throws SQLException
	 */
	private static void insert(DataSource shardingDataSource) throws SQLException {
		String sql = "insert into t_order (order_id,user_id,status) values(1,1,'test')";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = shardingDataSource.getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			log.info("插入数据1-1-test");
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			log.error(e.getLocalizedMessage());
		} finally {
			if (ps != null)
				ps.close();
            //如果conn不关掉，同一线程且同一数据库连接内，如有写入操作，以后的读操作均从主库读取，用于保证数据一致性。
			/*if (conn != null)
				conn.close();*/
		}
	}
	
	public static ShardingDataSource getShardingDataSource(DataSourceRule dataSourceRule) {
		// 表规则可以指定每张表在数据源中的分布情况
		TableRule orderTableRule = TableRule.builder("t_order").actualTables(Arrays.asList("t_order_0", "t_order_1"))
				.dataSourceRule(dataSourceRule).build();
		TableRule orderItemTableRule = TableRule.builder("t_order_item")
				.actualTables(Arrays.asList("t_order_item_0", "t_order_item_1")).dataSourceRule(dataSourceRule).build();
		ShardingRule shardingRule = ShardingRule.builder().dataSourceRule(dataSourceRule)
				.tableRules(Arrays.asList(orderTableRule, orderItemTableRule))
				// 绑定表代表一组表，这组表的逻辑表与实际表之间的映射关系是相同的。比如t_order与t_order_item就是这样一组绑定表关系,它们的分库与分表策略是完全相同的,那么可以使用它们的表规则将它们配置成绑定表
				.bindingTableRules(Collections
						.singletonList(new BindingTableRule(Arrays.asList(orderTableRule, orderItemTableRule))))
				.databaseShardingStrategy(
						new DatabaseShardingStrategy("user_id", new ModuloDatabaseShardingAlgorithm()))
				.tableShardingStrategy(new TableShardingStrategy("order_id", new ModuloTableShardingAlgorithm()))
				//多分片键值
//				.tableShardingStrategy(new TableShardingStrategy(Arrays.asList("order_id","user_id"), new MultipleKeysModuloTableShardingAlgorithm()))
				.build();
		return new ShardingDataSource(shardingRule);
	}


	public static DataSource createDataSource(final String dataSourceName) {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl(String.format("jdbc:mysql://123.56.118.135:3306/%s", dataSourceName));
		dataSource.setUsername("mobile");
		dataSource.setPassword("mobile");
		return dataSource;
	}
}
