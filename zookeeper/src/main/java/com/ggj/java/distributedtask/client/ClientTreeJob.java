package com.ggj.java.distributedtask.client;

import com.ggj.java.distributedtask.core.annation.DistributeJob;
import com.ggj.java.distributedtask.core.job.AbstractJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Random;


/**
 * @author:gaoguangjin
 * @date 2016/9/1 17:24
 */
@Slf4j
@DistributeJob(jobName="clientThree",groupName = "",triggerKey = "",jobCron = "0/20 * * * * ?",jobDetail = "分布式定时测试-clientThree")
@DisallowConcurrentExecution
public class ClientTreeJob extends AbstractJob {
    @Override
    public void beforeExecuteJob(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    }

    @Override
    public void executeJob(JobExecutionContext jobExecutionContext) throws JobExecutionException{
        log.info("开始执行job three");
        try {
            Random random=new Random();
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            log.error("",e);
        }
        log.info("结束执行job three");
    }

    @Override
    public void afterExecuteJob(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
