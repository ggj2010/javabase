package com.ggj.java.distributetask.core.registerycenter.zookeeper;

/**
 * @author:gaoguangjin
 * @date 2016/9/1 17:54
 */
public class ZKPath {
	
	public static final String LEADER_PATH = "/leader";
	private static final String JOB_NAME = "jobname";
	
	private static final String JOB_DATA = "jobdata";
	
	private static final String JOB_IP = "IPs";
	private static final String JOB_STATU = "jobstatu";
	private String clientId = "";
	private String jobNamePath;
	private String ip;

	public ZKPath(String clientId,String ip, String jobName) {
		this.clientId = clientId;
		this.ip = ip;
		this.jobNamePath = String.format("/%s/%s", clientId, jobName);
	}
	
	public String getJobDataPath() {
		return String.format("%s/%s", jobNamePath, JOB_DATA);
	}
	public String getJobIpDataPath() {
		return String.format("%s/%s/%s", jobNamePath, JOB_IP,ip);
	}
	public String getJobIpPath() {
		return String.format("%s/%s", jobNamePath, JOB_IP);
	}

	public String getJobStatuPath() {
		return String.format("%s/%s", jobNamePath, JOB_STATU);
	}
	
	public String getJobNamePath() {
        return jobNamePath;
    }
}
