package com.ggj.java.guava.eventbus;

import com.google.common.eventbus.EventBus;

/**
 * @author:gaoguangjin
 * @date:2018/2/27
 */
public class EventBusTest {
    private final  static String SEND_ORDER_MESSAGE="sendOrderMessage";

    public static void main(String[] args) {
        EventBus eventBus=new EventBus(SEND_ORDER_MESSAGE);
        eventBus.register(new OrderEventListener());
        eventBus.register(new EmailEventListener());

        eventBus.post("123");
    }

}
