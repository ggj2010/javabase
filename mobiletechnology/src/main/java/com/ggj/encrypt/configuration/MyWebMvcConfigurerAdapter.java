package com.ggj.encrypt.configuration;

import com.ggj.encrypt.interceptor.IpInterceptor;
import com.ggj.encrypt.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author:gaoguangjin
 * @Description:重写WebMvcConfigurerAdapter
 * @Email:335424093@qq.com
 * @Date 2016/4/26 15:06
 */
@Configuration
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new IpInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/mobile/**");
        super.addInterceptors(registry);
    }
}
