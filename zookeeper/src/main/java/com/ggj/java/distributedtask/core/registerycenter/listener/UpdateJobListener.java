package com.ggj.java.distributedtask.core.registerycenter.listener;

import com.ggj.java.distributedtask.core.job.JobManager;
import com.ggj.java.distributedtask.core.job.vo.JobConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * job 删除事件
 * @author:gaoguangjin
 * @date 2016/9/2 16:07
 */
@Slf4j
public class UpdateJobListener implements DataChangeListener {

    @Override
    public void eventHandler(Object... ojb) {
        JobConfig jobConfig = (JobConfig) ojb[0];
        JobManager.updateJob(jobConfig);
        log.info("update job");
    }
}
