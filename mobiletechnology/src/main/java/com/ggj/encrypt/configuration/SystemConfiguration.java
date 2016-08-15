package com.ggj.encrypt.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/26 18:22
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix ="system")
public class SystemConfiguration {
    //ip访问次数
    private String blackipLimitTime;
    private String isBlackipLimit;
    //ip最大访问次数
    private String ipMaxInvokeNumber;
    //半小时登陆错误次数限制调用次
    private String loginErrorMaxNumber;
    //是否启用enableSwagger
    private String enableSwagger;
}
