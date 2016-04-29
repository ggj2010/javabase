package com.ggj.encrypt.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author:gaoguangjin
 * @Description: redis配置
 * @Email:335424093@qq.com
 * @Date 2016/4/26 17:34
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix ="redis")
public class RedisConfiguration {
    private  String host;
    private  String port;
    private  String timeout;
    private  String passwords;

    private  String maxtotal;
    private  String maxidle;
    private  String minidle;
    private  String maxwaitmillis;
    private  String testonborrow;
    private  String testwhileidle;


}
