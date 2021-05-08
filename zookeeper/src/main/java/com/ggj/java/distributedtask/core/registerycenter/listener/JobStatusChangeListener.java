package com.ggj.java.distributedtask.core.registerycenter.listener;

import com.ggj.java.distributedtask.core.job.vo.JobConfig;
import com.ggj.java.distributedtask.core.job.JobManager;
import com.ggj.java.distributedtask.core.job.enums.JobExcuteStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;

/**
 * job状态变更监听
 * @author:gaoguangjin
 * @date 2016/9/2 16:07
 */
@Slf4j
public class JobStatusChangeListener implements DataChangeListener {

    @Override
    public void eventHandler(Object... ojb) throws SchedulerException {
        JobConfig jobConfig = (JobConfig)ojb[0];
        JobExcuteStatusEnum jobExcuteStatusEnum = (JobExcuteStatusEnum)ojb[1];
        log.info("update job excuteStatus jobName={} now status:{} ",jobConfig.getJobName(),jobExcuteStatusEnum.getName());
        switch (jobExcuteStatusEnum){
            case RUN:
                JobManager.runJob(jobConfig);
                break;
            case STOP:
                JobManager.deleteJob(jobConfig);
                break;
            case PAUSE:
                JobManager.pauseJob(jobConfig);
                break;
            default:
        }
    }
}
