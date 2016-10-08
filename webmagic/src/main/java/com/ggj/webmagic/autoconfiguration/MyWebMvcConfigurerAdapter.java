package com.ggj.webmagic.autoconfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author:gaoguangjin
 * @Description:重写WebMvcConfigurerAdapter
 * @Email:335424093@qq.com
 * @Date 2016/4/26 15:06
 */
@Configuration
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter  {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
    }
}
