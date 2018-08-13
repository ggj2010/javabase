package com.ggj.java.spring.event;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author:gaoguangjin
 * @date:2018/2/27
 */
@Slf4j
@Component
public class OrderListener implements ApplicationListener<OrderEvent> {
    @Override
    public void onApplicationEvent(OrderEvent orderEvent) {
        String message=orderEvent.getSource().toString();
        log.info("message:"+message);
    }
}
