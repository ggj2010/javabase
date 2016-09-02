package com.ggj.java.distributetask.core.registerycenter.zookeeper;


import com.ggj.java.distributetask.core.job.JobConfig;

/**
 * @author:gaoguangjin
 * @date 2016/9/1 11:18
 */
public interface RegistryCenter {
    void register(JobConfig ob);
}
