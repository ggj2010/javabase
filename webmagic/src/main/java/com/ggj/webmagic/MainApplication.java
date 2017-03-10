package com.ggj.webmagic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * nohup java -jar target/webmagic-0.0.1.jar -Xms400m -Xmx400m  -Dtieba.executeDeleteTiebaImageTask="fase" -Djava.awt.headless=true -XX:+UseParNewGC -XX:+UseConcMarkSweepGC
 * -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -XX:-HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/data/log  >/dev/null &
 * @author:gaoguangjin
 * @date 2016/8/19 10:52
 */
// 发现注解@Scheduled的任务并后台执行
@EnableScheduling
//搜索servlert
@ServletComponentScan
@SpringBootApplication
//扫描mybatis接口
@MapperScan("com.ggj.webmagic.tieba.dao")
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class,args);
    }
}
