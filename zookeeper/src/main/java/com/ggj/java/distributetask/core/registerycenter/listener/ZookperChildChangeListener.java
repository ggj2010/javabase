package com.ggj.java.distributetask.core.registerycenter.listener;

import com.ggj.java.distributetask.core.job.JobConfig;
import com.ggj.java.distributetask.core.job.JobManager;

/**
 * @author:gaoguangjin
 * @date 2016/9/2 16:07
 */
public class ZookperChildChangeListener implements DataChangeListener {

    @Override
    public void eventHandler(Object... ojb) {
        JobConfig jobConfig = (JobConfig)ojb[0];
        //JobManager.deleteJob(jobConfig);
    }
}
