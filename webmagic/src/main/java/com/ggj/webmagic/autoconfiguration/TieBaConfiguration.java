package com.ggj.webmagic.autoconfiguration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 贴吧Top爬虫配置项
 * @author:gaoguangjin
 * @date 2016/8/22 15:22
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix ="tieba")
public class TieBaConfiguration {
    private String[] tiebaName;
    private String tiebaTopUrl;
    //贴吧主页
    private String tiebaContentUrl;
    //贴吧帖子内容页面
    private String tiebaContentPageUrl;
    //贴吧帖子内容页面
    private String tiebaImageUrl;
    //贴吧帖子id  redis pub/sub topic名称
    private String tiebaContentIdTopic;
    //同步帖子最后一页
    private String tiebaContentPageEndNum;
}
