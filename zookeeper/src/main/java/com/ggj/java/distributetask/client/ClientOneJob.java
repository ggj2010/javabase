package com.ggj.java.distributetask.client;

import com.ggj.java.distributetask.core.annation.DistributeJob;
import com.ggj.java.distributetask.core.job.BaseJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import lombok.extern.slf4j.Slf4j;



/**
 * @author:gaoguangjin
 * @date 2016/9/1 17:24
 */
@Slf4j
@DistributeJob(jobName="clientOne",groupName = "groupone",triggerKey = "clientOneKey",jobCron = "0/30 * * * * ?",jobDetail = "分布式定时测试-clientOne")
public class ClientOneJob extends BaseJob  {
    @Override
    public void beforeExecuteJob(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    }

    public void executeJob(JobExecutionContext jobExecutionContext) throws JobExecutionException{
        log.info("开始执行job");
        try {
            Thread.sleep(1000*35);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("结束执行job");
    }

    @Override
    public void afterExecuteJob(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
