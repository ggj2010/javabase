package com.ggj.db.sharding.masterslave;

import com.dangdang.ddframe.rdb.sharding.api.HintManager;
import com.dangdang.ddframe.rdb.sharding.api.MasterSlaveDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.jdbc.ShardingDataSource;
import com.ggj.db.sharding.common.CommonUtil;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;



/**
 * http://dangdangdotcom.github.io/sharding-jdbc/02-guide/sharding/
 * 支持项：
 * 提供了一主多从的读写分离配置，可配合分库分表使用。
 * 同一线程且同一数据库连接内，如有写入操作，以后的读操作均从主库读取，用于保证数据一致性。(这个要注意下)
 * Spring命名空间。
 * 基于Hint的强制主库路由。
 *
 * @author:gaoguangjin
 * @date 2017/4/10 16:06
 */
@Slf4j
public class MasterSalve {
    public static void main(String[] args) {
        master();
    }

    private static void master() {
        try{
            ShardingDataSource shardingDataSource = getShardingDataSource();
            CommonUtil.crud(shardingDataSource);
            //注释上的crud,一般情况下查询是走从库的，如果使用了MasterRouteOnly就强制主库
//            hitMaster(shardingDataSource,false);
//            log.info("--------------强制主库--------------------");
//            hitMaster(shardingDataSource,true);
        }catch (Exception e){
            log.info("error,{}"+e.getLocalizedMessage());
        }
    }

    /**
     * 使用Hint强制路由主库示例
     * @param shardingDataSource
     */
    private static void hitMaster(ShardingDataSource shardingDataSource,boolean flag) throws  Exception{
        String sql = "SELECT * FROM t_order";
        try (HintManager hintManager = HintManager.getInstance();
             Connection connection = shardingDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            //强制路由主库
            if(flag)
             hintManager.setMasterRouteOnly();
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    CommonUtil.display(resultSet);
                }
            }
        }
    }

    /**
     * 主从的时候首先根据user_id 筛选出主数据源ds_0或者ds_1
     * 然后再根据sql类型 决定主或者从。
     * 可以一主多从，模式
     * @return
     */
    public static ShardingDataSource getShardingDataSource() {
        //创建两个数据源ds_0和ds_1
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        //master、salve、salve......
        //数据源1 一个主库、2个从库
        dataSourceMap.put("ds_0", MasterSlaveDataSourceFactory.createDataSource("这个name可以随便定义aa", CommonUtil.createDataSource("ds_0_master"), CommonUtil.createDataSource("ds_0_slave_0"), CommonUtil.createDataSource("ds_0_slave_1")));
        //数据源2 一个主库、2个从库
        dataSourceMap.put("ds_1", MasterSlaveDataSourceFactory.createDataSource("这个name可以随便定义bb", CommonUtil.createDataSource("ds_1_master"), CommonUtil.createDataSource("ds_1_slave_0"), CommonUtil.createDataSource("ds_1_slave_1")));
        DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap);
        return CommonUtil.getShardingDataSource(dataSourceRule);
    }
}
