package com.ggj.java.guava.eventbus;

import com.google.common.eventbus.EventBus;

/**
 * 使用eventbus可以实现发布者和订阅功能，可以实现业务解耦。
 * @author:gaoguangjin
 * @date:2018/2/27
 */
public class EventBusTest {
    private final  static String SEND_ORDER_MESSAGE="sendOrderMessage";

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        EventBus eventBus=new EventBus(SEND_ORDER_MESSAGE);
        //注册订阅者
        eventBus.register(new OrderEventListener());
        eventBus.register(new EmailEventListener());
        //发布事件
        eventBus.post("123");
    }
}
