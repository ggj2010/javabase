package com.ggj.db.splittable.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author:gaoguangjin
 * @date 2017/2/22 15:41
 */
@Getter
@Setter
public class Order {
    private int userId;
    private long orderId;
    private String orderName;
    private Date createTime;

    public Order(int userId,long orderId){
        this.userId=userId;
        this.orderId=orderId;
        this.orderName="商品"+userId;
        this.createTime=new Date();
    }
}
