package com.ggj.java.distributedtask.core.registerycenter.listener;

import com.ggj.java.distributedtask.core.job.JobManager;
import com.ggj.java.distributedtask.core.job.vo.JobConfig;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;

/**
 * 创建job
 *
 * @author:gaoguangjin
 * @date 2016/9/2 16:07
 */
@Slf4j
public class CreateJobListener implements DataChangeListener {
    @Override
    public void eventHandler(Object... ojb) {
        JobConfig jobConfig = (JobConfig) ojb[0];
        Integer jobStatus = (Integer) ojb[1];
        try {
            JobManager.initJob(jobStatus, jobConfig);
            log.info("create job");
        } catch (SchedulerException e) {
            log.error("create job error", e);
        }
    }
}
