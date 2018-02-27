package com.ggj.java.guava.eventbus;

import lombok.Getter;
import lombok.Setter;

/**
 * @author:gaoguangjin
 * @date:2018/2/27
 */
@Getter
@Setter
public class OrderEvent {
    private String message;
    private int orderId;

    public OrderEvent(String message, int orderId) {
        this.message = message;
        this.orderId = orderId;
    }
}
