package com.ggj.db.splittable.service;

import com.ggj.db.splittable.bean.Order;
import com.ggj.db.splittable.dao.OrderMapper;
import com.ggj.db.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ggj.db.splittable.dao.CreateTableMapper;

import java.util.*;


/**
 * 分表测试
 * 分表分为客户端分表和中间件分表
 * 客户端分库分表，在客户端完成分库分表操作，直连数据库
 *使用分库分表中间件，客户端连分库分表中间件，由中间件完成分库分表操作
 * http://mp.weixin.qq.com/s?__biz=MjM5MjAwODM4MA==&mid=2650686445&idx=1&sn=9117ee33bff27b128a287a6c751d3e32&scene=0#rd
 * @author:gaoguangjin
 * @date 2017/2/22 13:42
 */
@Service
@Transactional(readOnly = true)
public class SplitTableService {
	
	private final String TABLE_NAME = "tb_order_";
	
	private final String SINGLE_ORDER_TABLE_NAME = "tb_order";
	
	@Autowired
	private CreateTableMapper createTableMapper;

	@Autowired
	private OrderMapper orderMapper;

    /**
     *建立测试表tb_order和tb_order_0......tb_order_9
     * tb_order插入1000万数据
     * tb_order_0到tb_order_9各插入10万数据
     * 客户端取模分表操作
     */
	@Transactional(readOnly = false)
	public void prepareTableAndData() {
		// 假设有1000万条数据
		int orderSize = 10000 * 1000;
		// order表拆分成10份
		int splitTableSize = 10;
		// 准备数据
        List<Order> list=new ArrayList<>(10000);
        Map<Integer,List<Order>> map=new HashMap<>(10);
        IdWorker idWorker=new IdWorker(0,0);

        createTableMapper.createOrderTable(SINGLE_ORDER_TABLE_NAME);
        for(int i = 0; i < splitTableSize; i++) {
             createTableMapper.createOrderTable(TABLE_NAME + i);
        }
        for(int i=0;i<orderSize;i++){
            Order order=new Order(i, idWorker.getId());
            list.add(order);
            if(list.size()==10000){
                orderMapper.save(getSaveMapByNameAndList(SINGLE_ORDER_TABLE_NAME,list));
                list=new  ArrayList(10000);
            }
        }
		// 表编号 = uid % 10
        // 创建测试表 插入数据
//        for(int i = 0; i < 100; i++) {
           // createTableMapper.createOrderTable(TABLE_NAME + i);
//            orderMapper.save(getSaveMapByNameAndList(SINGLE_ORDER_TABLE_NAME,list.subList(i*10000,(i+1)*10000)));
//        }
	}
    private Map<String,Object> getSaveMapByNameAndList(String tableName, List<Order> list) {
        Map<String,Object> singTable=new HashMap<>();
        singTable.put("tableName",tableName);
        singTable.put("listOrder",list);
        return singTable;
    }
}
