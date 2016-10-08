package com.ggj.webmagic.autoconfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * @author:gaoguangjin
 * @date 2016/9/23 15:33
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "weixin")
public class WeiXinConfig {
	
	private String appid;
	private String secret;
	private String authorizeUrl;
	private String accessTokenUrl;
	private String userinfoUrl;
	private String authCallBackUrl;

}
