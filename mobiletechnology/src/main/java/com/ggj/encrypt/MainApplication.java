package com.ggj.encrypt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/25 13:37
 */
//发现注解@Scheduled的任务并后台执行
@EnableScheduling
@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
         SpringApplication.run(MainApplication.class);
    }
}
