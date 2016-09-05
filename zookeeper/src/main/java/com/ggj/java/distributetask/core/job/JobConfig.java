package com.ggj.java.distributetask.core.job;

import com.ggj.java.distributetask.core.LocalHostService;
import lombok.Getter;
import lombok.Setter;
import org.quartz.Job;

/**
 * @author:gaoguangjin
 * @date 2016/9/2 9:30
 */
@Getter
@Setter
public class JobConfig {
	
	
	private String clientId;
	
	private String jobName;
	
	private String groupName;
	
	private String triggerKey;
	
	private String jobDetail;
	
	private String jobCron;
	private String ip;

	private Class<? extends Job> jobClass;
	
	public JobConfig(String clientId, String jobName, String groupName, String triggerKey, String jobDetail,
			String jobCron, Class<? extends Job> jobClass) {
		this.clientId = clientId;
		this.jobName = jobName;
		this.groupName = groupName;
		this.triggerKey = triggerKey;
		this.jobDetail = jobDetail;
		this.jobCron = jobCron;
		this.jobClass=jobClass;
		this.ip=new LocalHostService().getIp();
	}
}
