package com.ggj.java.distributetask.core.job;

import com.ggj.java.distributetask.core.registerycenter.zookeeper.MasterSelector;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author:gaoguangjin
 * @date 2016/9/1 17:51
 */
//有状态的job
@DisallowConcurrentExecution
public abstract class BaseJob implements Job {
    public boolean checkIsMaster(){
        return false;
    }

    public abstract  void beforeExecuteJob(JobExecutionContext jobExecutionContext) throws JobExecutionException;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            this.beforeExecuteJob(jobExecutionContext);
        if(MasterSelector.isMaster())
             executeJob(jobExecutionContext);
            this.afterExecuteJob(jobExecutionContext);
    }

    public abstract  void afterExecuteJob(JobExecutionContext jobExecutionContext) throws JobExecutionException;
    public abstract  void executeJob(JobExecutionContext jobExecutionContext) throws JobExecutionException;
}
