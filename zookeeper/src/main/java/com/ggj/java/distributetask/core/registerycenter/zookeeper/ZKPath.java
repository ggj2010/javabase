package com.ggj.java.distributetask.core.registerycenter.zookeeper;

import lombok.Getter;


/**
 * @author:gaoguangjin
 * @date 2016/9/1 17:54
 */
public class ZKPath {
    private String clientId = "";
    private static final String  JOB_NAME="jobname";
    private static final String JOB_DATA="jobdata";
    private static final String JOB_STATU="jobstatu";
    public static final String LEADER_PATH="/leader";
    @Getter
    private static  String jobNamePath;
    public ZKPath(String clientId,String jobName){
        this.clientId=clientId;
        this.jobNamePath=String.format("/%s/%s", clientId, JOB_NAME);
    }

    public String getJobDataPath(){
        return String.format("%s/%s", jobNamePath, JOB_DATA);
    }
    public String getJobStatuPath(){
        return String.format("%s/%s", jobNamePath, JOB_STATU);
    }

}
