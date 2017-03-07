package com.ggj.webmagic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ggj.webmagic.elasticsearch.ElasticSearch;

import lombok.extern.slf4j.Slf4j;

/**
 * @author:gaoguangjin
 * @date 2016/8/19 11:01
 */
@Service
@Slf4j
public class ScheduleTask {
	
	@Autowired
	private WebmagicService webmagicService;
	
	@Autowired
	private ElasticSearch elasticSearch;
	
	private boolean isDeleteImageNow ;
	
	// 一个小时更新一次，执行top
	// @Scheduled(initialDelay = 0, fixedRate = 1000*60*60*6)
	// @Scheduled(cron="0 */60 * * * ?")
	public void scheduleUpdateTieBa() {
		try {
			webmagicService.addTieBaTop();
		} catch (Exception e) {
			log.error("贴吧同步TOP失败！" + e.getLocalizedMessage());
		}
	}
	
	@Scheduled(initialDelay = 0, fixedDelay = 1000*60*1)
	public void scheduleTieBaImage() {
		try {
            //删除历史图片时候不进行定时任务
			if (!isDeleteImageNow)
				webmagicService.addTieBaImage();
		} catch (Exception e) {
			log.error("贴吧同步Image失败！" + e.getLocalizedMessage());
		}
	}
	
	/**
	 * 将贴吧信息放到elasticsearch
	 */
	//@Scheduled(initialDelay = 0, fixedDelay = 1000*60*1)
	public void scheduleSearchIndex() {
		try {
			elasticSearch.addTieBaContentIndex();
		} catch (Exception e) {
			log.error("贴吧同步Image失败！" + e.getLocalizedMessage());
		}
	}
	
	/**
	 *因为七牛免费的存储空间就10G 如果一直爬的话就超了，所以三天清除下数据
	 * 两种方式删除
	 */
//	@Scheduled(cron = "0 0 */3 * * ?")
//	@Scheduled(initialDelay = 0, fixedDelay = 1000*60*60*12*3)
	public void deleteTieBaImage() {
		try {
			isDeleteImageNow = true;
//			webmagicService.deleteTieBaImageTypeOne();
			webmagicService.deleteTieBaImageTypeTwo();
			isDeleteImageNow = false;
		} catch (Exception e) {
			log.error("删除Image失败！" + e.getLocalizedMessage());
		} finally {
			isDeleteImageNow = false;
		}
	}

}
