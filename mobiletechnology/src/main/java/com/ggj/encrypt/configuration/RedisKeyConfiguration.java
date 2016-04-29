package com.ggj.encrypt.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/26 18:09
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "key",locations = "classpath:config/redis-key.yml")
public class RedisKeyConfiguration {
    //黑名单ip set类型
    private String blackip;
    //接口调用 string类型
    private String ipInvoke;
    //用户信息
    private String userInfoLoginName;
    //登陆错误次数限制
    private String loginErrorLimit;
    //token
    private String userToken;
}
