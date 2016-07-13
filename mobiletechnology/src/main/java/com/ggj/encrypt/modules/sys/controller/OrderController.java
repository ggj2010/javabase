package com.ggj.encrypt.modules.sys.controller;

import com.ggj.encrypt.common.persistence.Result;
import com.ggj.encrypt.configuration.ResultCodeConfiguration;
import com.ggj.encrypt.modules.sys.bean.Order;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggj.encrypt.modules.base.controller.BaseController;

import java.util.Arrays;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/5/3 11:22
 */
@RestController
@RequestMapping("/mobile/order")
@Slf4j
public class OrderController extends BaseController {

    @RequestMapping("/orderList/{currentPage}/{pageSize}")
    public String orderList(@PathVariable("currentPage")int currentPage,@PathVariable("pageSize")int pageSize) throws Exception {
        //TODO 模拟走数据库
        Order order1=new Order();
        Order order2=new Order();
        order1.setId("1");
        order2.setId("2");
        order1.setOrderDetail("衣服哈");
        order2.setOrderDetail("事务啊");
        return resultCodeConfiguration.getResult().addData( Arrays.asList(order1,order2));
    }
}
