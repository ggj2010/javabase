package com.ggj.java.distributetask.core.registerycenter.listener;

import com.ggj.java.distributetask.core.job.JobConfig;
import com.ggj.java.distributetask.core.job.JobManager;
import com.ggj.java.distributetask.core.job.JobStatu;
import com.ggj.java.distributetask.core.registerycenter.zookeeper.ZKPath;

/**
 * @author:gaoguangjin
 * @date 2016/9/2 16:07
 */
public class ZookperDataChangeListener implements DataChangeListener {

    @Override
    public void eventHandler(Object... ojb) {
        JobConfig jobConfig = (JobConfig)ojb[0];
        JobStatu jobStatu = (JobStatu)ojb[1];

        switch (jobStatu){
            case RUN:
                JobManager.initJob(jobConfig);
            case STOP:
                JobManager.deleteJob(jobConfig);
            case PAUSE:
                JobManager.pauseJob(jobConfig);
            default:
        }
    }
}
