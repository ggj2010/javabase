package com.ggj.webmagic.tieba;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ggj.webmagic.autoconfiguration.TieBaConfiguration;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 获取top经验排名
 * @author:gaoguangjin
 * @date 2016/8/18 19:07
 */
@Service
public class TopProcessor implements PageProcessor {
    private volatile static boolean isAddTarget = false;
    private static ConcurrentHashMap<String, TopBean> map = new ConcurrentHashMap<String, TopBean>();
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private TieBaConfiguration tieba;
    private String tiebaName;
    private String tiebaUrl;
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        for (int i = 1; i <= 20; i++) {
            String index = "";
            if (tiebaUrl.equals(page.getUrl().toString())) {
                if (i <= 3) {
                    index = i + "";
                } else {
                    index = page.getHtml().xpath(
                            "//tbody/tr[@class='drl_list_item'][" + (i+1) + "]/td[@class='drl_item_index']/p/text()")
                            .toString();
                }
            } else {
                index = page.getHtml().xpath(
                        "//tbody/tr[@class='drl_list_item'][" + (i + 1) + "]/td[@class='drl_item_index']/p/text()")
                        .toString();
            }
            if (StringUtils.isEmpty(index) || !StringUtils.isNumeric(index))
                continue;
            String name = page.getHtml().xpath(
                    "//tbody/tr[@class='drl_list_item'][" + (i + 1) + "]/td[@class='drl_item_name']/div/a/text()")
                    .toString();
            String level = page.getHtml().xpath(
                    "//tbody/tr[@class='drl_list_item'][" + (i + 1) + "]/td[@class='drl_item_title']/div/div@class")
                    .toString().replace("bg_lv", "");
            String experience = page.getHtml()
                    .xpath("//tbody/tr[@class='drl_list_item'][" + (i + 1) + "]/td[@class='drl_item_exp']/span/text()")
                    .toString();
            map.put(index, new TopBean(index, name, level, experience, tiebaName));
        }
        if (!isAddTarget) {
            String total = page.getHtml().xpath("//span[@class='drl_info_txt_gray'][1]/text()").toString();
            if (StringUtils.isNumeric(total)) {
                int pageSize = Integer.parseInt(total) / 20 + 1;
                for (int i = 2; i <= pageSize; i++) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(tiebaUrl).append("&pn=" + i);
                    page.addTargetRequests(Arrays.asList(sb.toString()));
                }
            }
            isAddTarget = true;
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public ConcurrentHashMap<String, TopBean> start(String name) {
        map.clear();
        tiebaName = name;
        tiebaUrl = tieba.getTiebaTopUrl() + name;
        Spider.create(this).addUrl(tiebaUrl).addPipeline(new ConsolePipeline())
                // 开启5个线程抓取
                .thread(200)
                // 启动爬虫
                .run();
        return map;
    }

}