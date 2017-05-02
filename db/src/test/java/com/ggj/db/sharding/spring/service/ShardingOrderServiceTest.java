package com.ggj.db.sharding.spring.service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ggj.db.sharding.spring.bean.ShardingOrder;
import com.ggj.db.splittable.com.ggj.base.BaseTest;

import static org.apache.coyote.http11.Constants.a;

/**
 * @author:gaoguangjin
 * @date 2017/5/2 15:00
 */
@Slf4j
public class ShardingOrderServiceTest extends BaseTest {
    private final String STATUS_NAME = "TEST";
    private final String STATUS_NAME_SLAVE = "INIT_SLAVE_0";
    @Autowired
    private ShardingOrderService shardingOrderService;
    private ShardingOrder order = new ShardingOrder();

    @Before
    public void befroe() {
        order.setUserId(1);
        order.setOrderId(1);
        order.setStatus(STATUS_NAME);
    }


    @Test
    public void delete() throws Exception {
        shardingOrderService.delete(order);
    }

    /**
     * 同一线程且同一数据库连接内，如有写入操作，以后的读操作均从主库读取，用于保证数据一致性。(这个要注意下)
     * ds_1_slave_0
     * t_order_1
     *
     * @throws Exception
     */
    @Test
    public void insert() throws Exception {
        shardingOrderService.insert(order);
    }

    /**
     * 因为是主从，查询的时候查的是从库
     * ds_1_slave_0
     * t_order_1
     *
     * @throws Exception
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void select() throws Exception {
        List<ShardingOrder> list = shardingOrderService.select(order);
        Assert.assertTrue(list.get(0).getStatus().equals(STATUS_NAME));
    }

    /**
     * ShardingOrder{userId=11, orderId=1100, status='INIT_SLAVE_0'}
     * @throws Exception
     */
    @Test
    public void selectSlave() throws Exception {
        order.setUserId(11);
        order.setOrderId(1100);
        List<ShardingOrder> list = shardingOrderService.select(order);
        log.info(list.get(0)+"");
        Assert.assertTrue(list.get(0).getStatus().equals(STATUS_NAME_SLAVE));
    }
}