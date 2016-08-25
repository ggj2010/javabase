package com.ggj.webmagic.tieba;

import com.alibaba.fastjson.JSONObject;
import com.ggj.webmagic.autoconfiguration.TieBaConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 爬虫获取帖子的id
 * 例如：http://tieba.baidu.com/p/4747953342，4747953342就是我们想要的id
 * @author:gaoguangjin
 * @date 2016/8/19 0:56
 */
@Service
@Slf4j
public class ContentIdProcessor implements PageProcessor {
    private volatile static boolean isAddTarget = false;
    //默认就同步2*50 100个帖子
    private  Integer endNum;
    private final  Integer pageSize = 50;
    private  static List<String> pageNumberList =new ArrayList<>();
    @Autowired
    private TieBaConfiguration tieBaConfiguration;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    static String tiebaUrl;
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    @Override
    public void process(Page page) {
        for (int i = 1; i <= pageSize; i++) {
            if (tiebaUrl.equals(page.getUrl().toString())&&i <= 3) {
                continue;
            }
            String json = page.getHtml().xpath("//ul[@id='thread_list']/li[@class='j_thread_list clearfix'][" + (i) + "]/@data-field").toString();
            if(json!=null&&JSONObject.parseObject(json).containsKey("id")){
                String pageId=JSONObject.parseObject(json).getString("id");
                pageNumberList.add(pageId);
               log.info("page = [" + pageId + "]");
            }
        }

        if (!isAddTarget) {
            for (int i = 2; i <= endNum; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append(tiebaUrl).append("&pn=" + i*pageSize);
                page.addTargetRequests(Arrays.asList(sb.toString()));
            }
            isAddTarget = true;
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

   public void start(String name) {
        pageNumberList.clear();
        tiebaUrl = tieBaConfiguration.getTiebaContentUrl() + name;
       endNum=Integer.parseInt(tieBaConfiguration.getTiebaContentPageEndNum());
        Spider.create(this).addUrl(tiebaUrl).addPipeline(new ConsolePipeline())
                // 开启5个线程抓取
                .thread(200)
                // 启动爬虫
                .run();
       redisTemplate.convertAndSend(tieBaConfiguration.getTiebaContentIdTopic(),JSONObject.toJSONString(pageNumberList));
    }
}
