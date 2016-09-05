package com.ggj.java.distributetask.client;

import com.ggj.java.curator.leader.selector.ClientSelector;
import com.ggj.java.distributetask.core.annation.DistributeJob;
import com.ggj.java.distributetask.core.job.JobConfig;
import com.ggj.java.distributetask.core.job.JobManager;
import com.ggj.java.distributetask.core.registerycenter.zookeeper.ZKConfig;
import com.ggj.java.distributetask.core.registerycenter.zookeeper.ZookeeperRegistryCenter;
import lombok.extern.slf4j.Slf4j;

/**
 *集群如果要保证 单个时间内只有一台机器调用， 那么久必须要中心化，需要一个server端来进行处理。
 * @author:gaoguangjin
 * @date 2016/9/2 9:14
 */
@Slf4j
public class JobClient {

    private static  final String CLIENT_ID="joboneid1";

//    public static final String connectString = "localhost:2180,localhost:2181,localhost:2182";
    public static final String connectString = "localhost:2185";
    public static final String nameSpace = "distributeJob";



    public static void main(String[] args) {
        ZookeeperRegistryCenter zookeeperRegistryCenter = new ZookeeperRegistryCenter();
        zookeeperRegistryCenter.init(new ZKConfig(connectString,nameSpace));
        DistributeJob dbj = ClientOneJob.class.getAnnotation(DistributeJob.class);
        JobConfig ob = new JobConfig(CLIENT_ID, dbj.jobName(), dbj.groupName(), dbj.triggerKey(), dbj.jobDetail(), dbj.jobCron(),ClientOneJob.class);
        //初始化job
        JobManager.initJob(ob);
        //zookper注册job
        zookeeperRegistryCenter.register(ob);
        zookeeperRegistryCenter.addLeader(CLIENT_ID);
    }


}
