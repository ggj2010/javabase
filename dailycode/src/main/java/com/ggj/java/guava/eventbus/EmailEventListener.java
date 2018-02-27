package com.ggj.java.guava.eventbus;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:gaoguangjin
 * @date:2018/2/27
 */
@Slf4j
public class EmailEventListener {
    @Subscribe
    public void sendMessage(String message){
        log.info("下单成功，发送邮件"+message);
    }
}
