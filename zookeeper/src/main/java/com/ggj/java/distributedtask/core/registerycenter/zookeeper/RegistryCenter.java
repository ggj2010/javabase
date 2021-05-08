package com.ggj.java.distributedtask.core.registerycenter.zookeeper;


import com.ggj.java.distributedtask.core.job.vo.JobConfig;

/**
 * @author:gaoguangjin
 * @date 2016/9/1 11:18
 */
public interface RegistryCenter {
    /**
     * 注册
     * @param ob
     */
    void register(JobConfig ob);

    /**
     * 去除临时节点
     */
    void removeTempNode();
}
