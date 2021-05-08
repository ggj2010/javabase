package com.ggj.java.distributedtask.core.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.ggj.java.distributedtask.core.util.LocalHostService;
import lombok.Getter;
import lombok.Setter;
import org.quartz.Job;

/**
 * @author:gaoguangjin
 * @date 2016/9/2 9:30
 */
@Getter
@Setter
@JSONType(ignores = {"ip"})
public class JobConfig {

    /**
     * 接入应用的唯一的客户端
     */
    private String clientId;
    /**
     * 任务名称 jobName为唯一标示
     */
    private String jobName;
    /**
     * 组名称
     */
    private String groupName;

    private String triggerKey;

    private String jobDetail;
    /**
     * job
     */
    private String jobCron;

    private String ip;
    /**
     * 任务执行超时推出时间
     */
    long excuteTimeOut;

    private Class<? extends Job> jobClass;

    public JobConfig() {
    }

    public JobConfig(String clientId, String jobName, String groupName, String triggerKey, String jobDetail,
                     String jobCron, Class<? extends Job> jobClass, long excuteTimeOut) {
        this.clientId = clientId;
        this.jobName = jobName;
        this.groupName = groupName == null || groupName.equals("") ? "defaultGroup" : groupName;
        this.triggerKey = triggerKey == null || triggerKey.equals("") ? jobName : triggerKey;
        this.jobDetail = jobDetail;
        this.jobCron = jobCron;
        this.jobClass = jobClass;
        this.ip = new LocalHostService().getIp();
        this.excuteTimeOut = excuteTimeOut;
    }


    @Override
    public String toString() {
        return "JobConfig{" +
                "clientId='" + clientId + '\'' +
                ", jobName='" + jobName + '\'' +
                ", groupName='" + groupName + '\'' +
                ", triggerKey='" + triggerKey + '\'' +
                ", jobDetail='" + jobDetail + '\'' +
                ", jobCron='" + jobCron + '\'' +
                ", ip='" + ip + '\'' +
                ", jobClass=" + jobClass +
                '}';
    }
}
