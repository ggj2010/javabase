package com.ggj.db.sharding.phone;

import com.dangdang.ddframe.rdb.sharding.api.MasterSlaveDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.jdbc.ShardingConnection;
import com.dangdang.ddframe.rdb.sharding.jdbc.ShardingDataSource;
import com.ggj.db.sharding.common.CommonUtil;
import com.ggj.db.sharding.common.ModuloTableShardingAlgorithm;
import com.ggj.db.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gaoguangjin
 */
@Slf4j
public class TestPhone {
    private static ShardingDataSource shardingDataSource = getShardingDataSource();
    private static SnowFlake snowFlake = new SnowFlake(0, 1);

    public static void main(String[] args) {
//        insert();
        select();
    }

    private static void select() {
        try {
            ResultSet rs = shardingDataSource.getConnection().createStatement().executeQuery("select * from phone where UnifiedID='100459878295801856%7959' and Shardkey=7959");
            while(rs.next()){
                log.info("UnifiedID={},Shardkey={},Phone={}",rs.getString(1),rs.getInt(2),rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insert() {
        //11位置
        String phone = "18638217959";
        String phone2 = "18638217958";
        insertToDB(phone);
        insertToDB(phone2);
    }

    private static void insertToDB(String phone) {
        String sql = "  INSERT IGNORE INTO `phone`(`Shardkey`,`UnifiedID`,`Phone`,`TmpPhone`,`AddTime`,`UpdateTime`) VALUES " +
                "(?,?,?,?,?,?)";
        try (Connection connection = shardingDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, getShardKey(phone));
            preparedStatement.setString(2, snowFlake.nextId() + "%" + getShardKey(phone));
            preparedStatement.setString(3, EncryptionUtilsV3.encrypt(phone));
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            preparedStatement.setString(6, DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            int number = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int getShardKey(String originPhone) {
        int shardKey = 0;
        String subPhone = originPhone.substring(originPhone.length() - 4, originPhone.length());
        for (int i = 0; i < subPhone.length(); i++) {
            if (subPhone.charAt(i) >= '0' && subPhone.charAt(i) <= '9') {
                shardKey = shardKey * 10 + subPhone.charAt(i) - '0';
            } else {
                //'-' 和 '_' 当做0处理
                shardKey = shardKey * 10;
            }
        }
        return shardKey;
    }

    public static ShardingDataSource getShardingDataSource() {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        //master、salve、salve......
        //数据源1 主从用一个库
        dataSourceMap.put("sms", MasterSlaveDataSourceFactory.createDataSource("sms", CommonUtil.createDataSource("sms"), CommonUtil.createDataSource("sms")));
        DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap);
        return getShardingDataSource(dataSourceRule);
    }

    public static ShardingDataSource getShardingDataSource(DataSourceRule dataSourceRule) {
        // 表规则可以指定每张表在数据源中的分布情况
        TableRule orderTableRule = TableRule.builder("phone").actualTables(Arrays.asList("phone_0", "phone_1"))
                .dataSourceRule(dataSourceRule).build();
        ShardingRule shardingRule = ShardingRule.builder().dataSourceRule(dataSourceRule)
                .tableRules(Arrays.asList(orderTableRule))
                .tableShardingStrategy(new TableShardingStrategy("Shardkey", new ModuloTableShardingAlgorithm()))
                .build();
        return new ShardingDataSource(shardingRule);
    }
}
