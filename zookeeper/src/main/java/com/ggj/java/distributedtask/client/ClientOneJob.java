package com.ggj.java.distributedtask.client;

import com.ggj.java.distributedtask.core.annation.DistributeJob;
import com.ggj.java.distributedtask.core.job.AbstractJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import lombok.extern.slf4j.Slf4j;


/**
 * @author:gaoguangjin
 * @date 2016/9/1 17:24
 */
@Slf4j
//配置每次都以本地启动的参数为准
@DistributeJob(jobName="clientOne",groupName = "",triggerKey = "",jobCron = "0/5 * * * * ?",jobDetail = "分布式定时测试-clientOne",excuteTimeOut = 1)
public class ClientOneJob extends AbstractJob {
    @Override
    public void beforeExecuteJob(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    }

    @Override
    public void executeJob(JobExecutionContext jobExecutionContext) throws JobExecutionException{
        log.info("开始执行job one");
        try {
            //Thread.sleep(1000*60*2);
            Thread.sleep(1000*6);
        } catch (InterruptedException e) {
            log.error("",e);
        }
        log.info("结束执行job one");
    }

    @Override
    public void afterExecuteJob(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
