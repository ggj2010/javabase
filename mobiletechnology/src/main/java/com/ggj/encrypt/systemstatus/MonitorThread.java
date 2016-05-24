package com.ggj.encrypt.systemstatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author:gaoguangjin
 * @date 2016/5/23 16:24
 */
@Service
@Slf4j
public class MonitorThread {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	
	private ThreadPoolExecutor threadPoolExecutor;
	
	//@Scheduled(initialDelay = 0, fixedRate = 6000)
	public void schedule() {
		if (threadPoolExecutor == null)
			threadPoolExecutor = threadPoolTaskExecutor.getThreadPoolExecutor();
		// TODO 可以j将线程信息 存到redis 或者放到某个地方，到时候可以实时刷新展示
		log.info("monitoring. monitorBeanName={} activeCount={} taskCount={} " + "completedTaskCount={} poolSize={} largestPoolSize={} "
				+ "queueSize={} queueRemainCapacity={}", "MonitorThread", threadPoolExecutor.getActiveCount(), threadPoolExecutor.getTaskCount(),
				threadPoolExecutor.getCompletedTaskCount(), threadPoolExecutor.getCorePoolSize(), threadPoolExecutor.getLargestPoolSize(),
				threadPoolExecutor.getQueue().size(), threadPoolExecutor.getQueue().remainingCapacity());
	}
}
