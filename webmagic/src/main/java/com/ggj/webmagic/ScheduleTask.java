package com.ggj.webmagic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ggj.webmagic.autoconfiguration.TieBaConfiguration;
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
	
	@Autowired
	private TieBaConfiguration tieBaConfiguration;
	
	public static volatile boolean execute;
	
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
	
	@Scheduled(initialDelay = 30, fixedDelay = 1000 * 60)
	public void scheduleTieBaImage() {
		try {
			// 删除历史图片时候不进行定时任务
			if (execute)
				webmagicService.addTieBaImage();
		} catch (Exception e) {
			log.error("贴吧同步Image失败！" + e.getLocalizedMessage());
		}finally {
			//阿里云服务器流量收费消耗太厉害，改成访问才出发定时任务
			//execute=false;
		}
	}
	
	/**
	 * 将贴吧信息放到elasticsearch
	 */
	@Scheduled(initialDelay = 1000 * 60, fixedDelay = 1000 * 60 * 1)
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
	// @Scheduled(cron = "0 0 */3 * * ?")
	@Scheduled(initialDelay = 0, fixedDelay = 1000 * 60 * 60 * 12 * 1)
	public void deleteTieBaImage() {
		try {
			if (tieBaConfiguration.getExecuteDeleteTiebaImageTask().equals("true")) {
				// 执行删除之前停掉 抓取图片的定时任务不执行
				execute = false;
				// 这种方式不建议使用 废弃
				// webmagicService.deleteTieBaImageTypeOne();
				webmagicService.deleteTieBaImageTypeTwo();
				execute = true;
			}else{
				log.info("第一次启动不执行删除定时任务");
			}
		} catch (Exception e) {
			log.error("删除Image失败！" + e.getLocalizedMessage());
		} finally {
			execute = true;
			tieBaConfiguration.setExecuteDeleteTiebaImageTask("true");
		}
	}
}
