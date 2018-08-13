package com.ggj.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Executors;

@EnableScheduling
@ServletComponentScan
@EnableConfigurationProperties
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        // 三种启动方式
        ConfigurableApplicationContext dd = SpringApplication.run(Main.class);
    }
}
