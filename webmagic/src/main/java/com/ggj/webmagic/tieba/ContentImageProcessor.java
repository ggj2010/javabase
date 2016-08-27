package com.ggj.webmagic.tieba;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSONObject;
import com.ggj.webmagic.WebmagicService;
import com.ggj.webmagic.autoconfiguration.TieBaConfiguration;
import com.ggj.webmagic.tieba.bean.ContentBean;
import com.ggj.webmagic.util.QiNiuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;


/**
 * @author:gaoguangjin
 * @date 2016/8/24 14:36
 */
@Service
@Slf4j
public class ContentImageProcessor implements PageProcessor {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private TieBaConfiguration tieBaConfiguration;
    @Autowired
    private QiNiuUtil qiNiuUtil;
    private static String url;
    private static String tiebaName;
    private volatile static boolean isAddTarget = false;
    private static List<String> pageNumberList;
    private static ConcurrentHashMap<byte[], byte[]> map = new ConcurrentHashMap<byte[], byte[]>();
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(5000);

    @Override
    public void process(Page page) {
        List<String> imageUrlList = page.getHtml().$(".BDE_Image", "src").all();
        String pageId = page.getUrl().toString().replace(tieBaConfiguration.getTiebaContentPageUrl(),"");
        List<String> list = new ArrayList<>();
        for (String imageUrl : imageUrlList) {
            if (imageUrl.startsWith(tieBaConfiguration.getTiebaImageUrl())) {
                imageUrl=convertImageUrl(imageUrl);
                if (null!=imageUrl)list.add(imageUrl);
            }
        }
        if (list.size() > 0) {
            map.put(WebmagicService.getByte(pageId), WebmagicService.getByte(JSONObject.toJSONString(list)));
        }else{
            redisTemplate.convertAndSend(tieBaConfiguration.getTiebaContentNoImageIdTopic(), JSONObject.toJSONString(new ContentBean(pageId,tiebaName)));
        }
        if (!isAddTarget) {
            for (String id : pageNumberList) {
                StringBuilder sb = new StringBuilder();
                sb.append(url).append(id);
                page.addTargetRequests(Arrays.asList(sb.toString()));
            }
            isAddTarget = true;
        }
    }

    /**
     * 百度图片禁止图片外链，所以需要自己上传到七牛
     * @param imageUrl
     */
    private String convertImageUrl(String imageUrl) {
        try {
            return qiNiuUtil.upload(imageUrl.replace(tieBaConfiguration.getTiebaImageUrl(),""),imageUrl);
        } catch (IOException e) {
           log.error("百度图片转换失败："+e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public Site getSite() {
        return site;
    }

    public ConcurrentHashMap<byte[], byte[]> start(List<String> pageNumberList, String tiebaName) {
        isAddTarget=false;
        map.clear();
        this.tiebaName=tiebaName;
        this.pageNumberList = pageNumberList;
        this.url = tieBaConfiguration.getTiebaContentPageUrl();
        Spider.create(this).addUrl(url).addPipeline(new ConsolePipeline())
                // 开启5个线程抓取
                .thread(10)
                // 启动爬虫
                .run();
        return  map;
    }
}
