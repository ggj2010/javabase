
package com.ggj.java.spring.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author:gaoguangjin
 * @date:2018/2/27
 */
public class OrderEvent extends ApplicationEvent {
    public OrderEvent(String jsonMessage) {
        super(jsonMessage);
    }
}
