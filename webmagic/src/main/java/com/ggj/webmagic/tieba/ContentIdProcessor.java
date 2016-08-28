package com.ggj.webmagic.tieba;

import com.alibaba.fastjson.JSONObject;
import com.ggj.webmagic.autoconfiguration.TieBaConfiguration;
import com.ggj.webmagic.tieba.bean.ContentBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
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
import java.util.Date;
import java.util.List;

import static javafx.scene.input.KeyCode.H;


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
    private  static List<ContentBean> pageNumberList =new ArrayList<>();
    @Autowired
    private TieBaConfiguration tieBaConfiguration;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static String tiebaUrl;
    private static String tiebaName;
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(5000);
    @Override
    public void process(Page page) {
        for (int i = 1; i <= pageSize; i++) {
            String json = page.getHtml().xpath("//ul[@id='thread_list']/li[@class='j_thread_list clearfix'][" + (i) + "]/@data-field").toString();
            if(json!=null&&JSONObject.parseObject(json).containsKey("id")){
                String pageId=JSONObject.parseObject(json).getString("id");
                String date = praseDate(page,i);
                pageNumberList.add(new ContentBean(pageId,date,tiebaName));
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

    /**
     * 帖子最后更新日期，只能比较当前年份的的帖子
     * 日期格式 11-20  8-25  17:33
     * @param page
     * @param i
     * @return
     */
    private String praseDate(Page page, int i) {
        String date= page.getHtml().xpath("//ul[@id='thread_list']/li[@class='j_thread_list clearfix'][" + (i) + "]//span[@class='threadlist_reply_date pull_right j_reply_data']/text()").toString();
        if(!StringUtils.isEmpty(date)){
            if(date.contains("-"))
                date=date.replace("-","").trim()+"0000";
            //17:33 当天的帖子
            if(date.contains(":"))
                date= DateFormatUtils.format(new Date(),"MMdd")+date.replace(":","").trim();
        }
        return  date;
    }

    @Override
    public Site getSite() {
        return site;
    }

   public void start(String tiebaName) {
       isAddTarget=false;
        pageNumberList.clear();
       this.tiebaName=tiebaName;
        tiebaUrl = tieBaConfiguration.getTiebaContentUrl() + tiebaName;
       endNum=Integer.parseInt(tieBaConfiguration.getTiebaContentPageEndNum());
        Spider.create(this).addUrl(tiebaUrl).addPipeline(new ConsolePipeline())
                // 开启5个线程抓取
                .thread(100)
                // 启动爬虫
                .run();
       if(pageNumberList.size()>0) {
           redisTemplate.convertAndSend(tieBaConfiguration.getTiebaContentIdTopic(), JSONObject.toJSONString(pageNumberList));
       }
    }
}
