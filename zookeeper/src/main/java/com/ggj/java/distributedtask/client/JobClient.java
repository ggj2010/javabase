package com.ggj.java.distributedtask.client;

import com.ggj.java.distributedtask.core.annation.DistributeJob;
import com.ggj.java.distributedtask.core.job.ShutDownHook;
import com.ggj.java.distributedtask.core.job.vo.JobConfig;
import com.ggj.java.distributedtask.core.job.JobManager;
import com.ggj.java.distributedtask.core.registerycenter.zookeeper.MasterSelector;
import com.ggj.java.distributedtask.core.registerycenter.zookeeper.vo.ZKConfig;
import com.ggj.java.distributedtask.core.registerycenter.zookeeper.ZookeeperRegistryCenter;
import com.ggj.java.distributedtask.core.scanner.JobScanner;
import com.ggj.java.distributedtask.core.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.SchedulerException;

import java.io.IOException;
import java.util.Map;

/**
 * 集群如果要保证 单个时间内只有一台机器调用， 那么久必须要中心化，需要一个server端来进行处理。
 *
 * @author:gaoguangjin
 * @date 2016/9/2 9:14
 */
@Slf4j
public class JobClient {

    private static final String CLIENT_ID = "seg-homework-api";

    //    public static final String connectString = "localhost:2180,localhost:2181,localhost:2182";
    public static final String CONNECT_URL = "localhost:2181";
    public static final String NAME_SPACE = "distributeJob";


    public static void main(String[] args) {
        try {
            System.setProperty(Constants.APPID, CLIENT_ID);
            ZookeeperRegistryCenter zookeeperRegistryCenter = ZookeeperRegistryCenter.getInstance();
            zookeeperRegistryCenter.init(new ZKConfig(CONNECT_URL, NAME_SPACE));
            Map<Class<?>, DistributeJob> map = JobScanner.scannerWithPath("com.ggj.java.distributedtask.client");
            if (map == null) {
                log.warn("job is empty");
            } else {
                for (Map.Entry<Class<?>, DistributeJob> classDistributeJobEntry : map.entrySet()) {
                    //注册job
                    regisJob(zookeeperRegistryCenter, classDistributeJobEntry.getValue(), classDistributeJobEntry.getKey());
                }
            }
            //选举
            zookeeperRegistryCenter.addLeader(CLIENT_ID);
            ShutDownHook.addShutDownHook();
        } catch (Exception e) {
            log.error("init jobclient erro", e);
        }
    }

    private static void regisJob(ZookeeperRegistryCenter zookeeperRegistryCenter, DistributeJob distributeJob, Class<?> clazz) throws SchedulerException {
        JobConfig jobConfig = new JobConfig(System.getProperty(Constants.APPID), distributeJob.jobName(), distributeJob.groupName(), distributeJob.triggerKey(), distributeJob.jobDetail(), distributeJob.jobCron(), (Class<? extends Job>) clazz,distributeJob.excuteTimeOut());
        //zookeeper注册job
       zookeeperRegistryCenter.register(jobConfig);
    }
}
