package com.ggj.db.sharding.common;

import java.util.*;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.MultipleKeysTableShardingAlgorithm;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

/**
 * 多分片 尽量少用
 * @author:gaoguangjin
 * @date 2017/4/6 21:24
 */
public final class MultipleKeysModuloTableShardingAlgorithm implements MultipleKeysTableShardingAlgorithm {

    @Override
    public Collection<String> doSharding(final Collection<String> availableTargetNames, final Collection<ShardingValue<?>> shardingValues) {
        System.out.println("ModuloTableShardingAlgorithm shardingValue.getValue():" + shardingValues);
        Set<Integer> orderIdValueSet = getShardingValue(shardingValues, "order_id");
        Set<Integer> userIdValueSet = getShardingValue(shardingValues, "user_id");

        List<String> result = new ArrayList<>();
        /*
        userIdValueSet[10,11] + orderIdValueSet[101,102] => valueResult[[10,101],[10,102],[11,101],[11,102]]
         */
        Set<List<Integer>> valueResult = Sets.cartesianProduct(userIdValueSet, orderIdValueSet);
        for (List<Integer> value : valueResult) {
            //多个分片
//            t_order_00                      user_id以a偶数   order_id为偶数
//            ├── t_order_01               user_id以a偶数   order_id为奇数
//            ├── t_order_10               user_id以b奇数   order_id为偶数
//            └── t_order_11               user_id以b奇数   order_id为奇数
            String suffix = Joiner.on("").join(value.get(0) % 2, value.get(1) % 2);
            for (String tableName : availableTargetNames) {
                if (tableName.endsWith(suffix)) {
                    result.add(tableName);
                    System.out.println("MultipleKeysModuloTableShardingAlgorithm :tableName:"+tableName);
                }
            }
        }
        return result;
    }

    private Set<Integer> getShardingValue(final Collection<ShardingValue<?>> shardingValues, final String shardingKey) {
        Set<Integer> valueSet = new HashSet<>();
        ShardingValue<Integer> shardingValue = null;
        for (ShardingValue<?> each : shardingValues) {
            if (each.getColumnName().equals(shardingKey)) {
                shardingValue = (ShardingValue<Integer>) each;
                break;
            }
        }
        if (null == shardingValue) {
            return valueSet;
        }
        switch (shardingValue.getType()) {
            case SINGLE:
                valueSet.add(shardingValue.getValue());
                break;
            case LIST:
                valueSet.addAll(shardingValue.getValues());
                break;
            case RANGE:
                for (Integer i = shardingValue.getValueRange().lowerEndpoint(); i <= shardingValue.getValueRange().upperEndpoint(); i++) {
                    valueSet.add(i);
                }
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return valueSet;
    }
}
