package com.ggj.java.distributedtask.core.registerycenter.zookeeper.vo;

/**
 * @author:gaoguangjin
 * @date 2016/9/1 17:54
 */
public class ZKJobPath {
    public static final String LEADER_PATH = "/leader";
    private static final String JOB_STATU = "jobstatu";
    private static final String JOB = "job";
    private static final String RUNNING_JOB_NUM= "runnningjobnum";
    private String jobNamePath;
    private String jobRootPath;
    private String ip;
    private String clientId;
    private String clientPath;

    public String getJobNamePath() {
        return jobNamePath;
    }

    public ZKJobPath(String clientId, String ip, String jobName) {
        this.ip = ip;
        this.clientId = clientId;
        this.jobNamePath = String.format("/%s/%s/%s", clientId, JOB,jobName);
        this.clientPath=String.format("/%s/%s", clientId,JOB);
    }

    public ZKJobPath(String clientId, String ip) {
        this.ip = ip;
        this.clientId = clientId;
        this.jobRootPath =  String.format("/%s/%s", clientId, JOB);
    }

    /**
     * 执行任务的服务器
     * /seg-homework-api/runnningjobnum
     * @return
     */
    public String getRunningJob() {
        return String.format("/%s/%s", clientId, RUNNING_JOB_NUM);
    }
    /**
     * 服务器正在执行中任务的数量
     * /seg-homework-api/runnningjobnum/10.145.107.1475032934
     * @return
     */
    public String getRunningJobNum() {
        return String.format("/%s/%s/%s", clientId, RUNNING_JOB_NUM,ip);
    }

    /**
     * /seg-homework-api/job/clientThree/jobstatu
     * @return
     */
    public String getJobStatuPath() {
        return String.format("%s/%s", jobNamePath, JOB_STATU);
    }

    public String getClientPath() {
        return clientPath;
    }

    public String getJobRootPath() {
        return jobRootPath;
    }
}
