package com.ggj.java.distributedtask.core.job;

import com.ggj.java.distributedtask.core.registerycenter.zookeeper.MasterSelector;
import com.ggj.java.distributedtask.core.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Quartz定时任务默认都是并发执行的，
 *
 * @author:gaoguangjin
 * @date 2016/9/1 17:51
 */
//@DisallowConcurrentExecution 禁止并发执行多个相同定义的JobDetail,
@Slf4j
public abstract class AbstractJob implements Job {

    public abstract void beforeExecuteJob(JobExecutionContext jobExecutionContext) throws JobExecutionException;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        this.beforeExecuteJob(jobExecutionContext);
        MasterSelector masterSelector = MasterSelector.getInstance();
        if (masterSelector.isMaster()) {
            masterSelector.increaseJobNum();
            try {
                long excuteTimeOut = jobExecutionContext.getJobDetail().getJobDataMap().getLong(Constants.TASK_EXCUETE_TIMEOUT_KEY);
                Future<?> future = ExcuteTaskThreadPool.getThreadPoolExecutor().submit(new Thread(() -> {
                    try {
                        executeJob(jobExecutionContext);
                    } catch (JobExecutionException e) {
                        log.error("excute job error");
                    }
                }));
                future.get(excuteTimeOut, TimeUnit.MINUTES);
            } catch (Exception e) {
                log.error("excute job timeout", e);
                //TODO 报警监控
            }  finally {
                masterSelector.decreaseJobNum();
            }
        }
        this.afterExecuteJob(jobExecutionContext);
    }

    public abstract void afterExecuteJob(JobExecutionContext jobExecutionContext) throws JobExecutionException;

    public abstract void executeJob(JobExecutionContext jobExecutionContext) throws JobExecutionException;
}
