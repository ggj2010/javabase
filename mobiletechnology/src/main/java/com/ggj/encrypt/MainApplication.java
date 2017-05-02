package com.ggj.encrypt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/25 13:37
 */
// 发现注解@Scheduled的任务并后台执行
@EnableScheduling
//搜索servlert
@ServletComponentScan
@SpringBootApplication
//扫描mybatis接口
@MapperScan("com.ggj")
public class MainApplication {
	public static void main(String[] args) {
		// 三种启动方式
		
		 SpringApplication.run(MainApplication.class);

        //启动二 fluent api
		// new SpringApplicationBuilder().sources(Parent.class).child(MainApplication.class).bannerMode(Banner.Mode.OFF).run(args);
		
		// SpringApplication app = new SpringApplication(MainApplication.class);
		// app.setBannerMode(Banner.Mode.OFF);
		// app.run(args);
	}
}
