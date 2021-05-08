package com.ggj.java.distributedtask.core.registerycenter.zookeeper;

import com.alibaba.fastjson.JSONObject;
import com.ggj.java.distributedtask.core.job.JobManager;
import com.ggj.java.distributedtask.core.job.enums.JobStatusEnum;
import com.ggj.java.distributedtask.core.job.vo.JobConfig;
import com.ggj.java.distributedtask.core.job.enums.JobExcuteStatusEnum;
import com.ggj.java.distributedtask.core.registerycenter.listener.CreateJobListener;
import com.ggj.java.distributedtask.core.registerycenter.listener.DataChangeListener;
import com.ggj.java.distributedtask.core.registerycenter.listener.DeleteJobListener;
import com.ggj.java.distributedtask.core.registerycenter.listener.JobStatusChangeListener;
import com.ggj.java.distributedtask.core.registerycenter.listener.UpdateJobListener;
import com.ggj.java.distributedtask.core.registerycenter.zookeeper.vo.ZKConfig;
import com.ggj.java.distributedtask.core.registerycenter.zookeeper.vo.ZKJobPath;
import com.ggj.java.distributedtask.core.util.Constants;
import com.ggj.java.distributedtask.core.util.CuratorUtil;
import com.ggj.java.distributedtask.core.util.LocalHostService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * zk注册中心
 *
 * @author:gaoguangjin
 * @date 2016/9/1 17:21
 */
@Slf4j
public class ZookeeperRegistryCenter implements RegistryCenter {

    private static final Map<Integer, DataChangeListener> listenerMap = new ConcurrentHashMap<>();

    @Getter
    private CuratorFramework client;

    private ZookeeperRegistryCenter() {
    }

    public static ZookeeperRegistryCenter getInstance() {
        return ZookeeperRegistryCenterSingletonHolder.zookeeperRegistryCenter;
    }

    @Override
    public void removeTempNode() {
        ZKJobPath zkJobPath = new ZKJobPath(System.getProperty(Constants.APPID), new LocalHostService().getIp(), "");
        try {
            CuratorUtil.deleteNode(client, zkJobPath.getRunningJobNum());
        } catch (Exception e) {
            log.error("remove tempnode error", e);
        }
    }

    private static class ZookeeperRegistryCenterSingletonHolder {
        public static ZookeeperRegistryCenter zookeeperRegistryCenter = new ZookeeperRegistryCenter();
    }

    public void init(ZKConfig zkConfig) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //client = CuratorFrameworkFactory.newClient(zkConfig.getConnectString(), retryPolicy);
        client = CuratorFrameworkFactory.builder().namespace(zkConfig.getNameSpace()).retryPolicy(retryPolicy).connectString(zkConfig.getConnectString()).build();
        client.start();
        listenerMap.put(JobStatusEnum.CREATE.getStatus(), new CreateJobListener());
        listenerMap.put(JobStatusEnum.DELETE.getStatus(), new DeleteJobListener());
        listenerMap.put(JobStatusEnum.UPDATE.getStatus(), new UpdateJobListener());
        JobStatusChangeListener jobStatusChangeListener = new JobStatusChangeListener();
        for (JobExcuteStatusEnum value : JobExcuteStatusEnum.values()) {
            listenerMap.put(value.getStatus(), jobStatusChangeListener);
        }
    }

    @Override
    public void register(JobConfig jobConfig) {
        Integer jobStatus = JobExcuteStatusEnum.RUN.getStatus();
        ZKJobPath zkJobPath = new ZKJobPath(jobConfig.getClientId(), jobConfig.getIp(), jobConfig.getJobName());
        try {
            // /clientId/jobName/
            byte[] jobRegisterInfo = CuratorUtil.createOrGet(client, zkJobPath.getJobNamePath(), JSONObject.toJSONString(jobConfig));
            if (jobRegisterInfo != null) {
                jobConfig = JSONObject.parseObject(CuratorUtil.getString(jobRegisterInfo), JobConfig.class);
            }
            // /clientId/jobName/jobstatu/
            byte[] jobStatusByte = CuratorUtil.createOrGet(client, zkJobPath.getJobStatuPath(), String.valueOf(JobExcuteStatusEnum.RUN.getStatus()));
            //job执行状态
            if (jobStatusByte != null) {
                jobStatus = CuratorUtil.getInteger(jobStatusByte);
            }
            // /clientId/runnningjobnum/
            CuratorUtil.create(client, CreateMode.EPHEMERAL, zkJobPath.getRunningJobNum(), String.valueOf(0));
            //添加子节点监听
            addChildWatcher(zkJobPath);
            //添加节点值变动监听
            addStatusWatcher(zkJobPath);
            //初始化job
            JobManager.initJob(jobStatus, jobConfig);
        } catch (Exception e) {
            log.error("注册失败！" + e.getLocalizedMessage());
        }
    }


    private void addStatusWatcher(ZKJobPath zkJobPath) throws Exception {
        final NodeCache nodeCache = new NodeCache(client, zkJobPath.getJobStatuPath(), false);
        nodeCache.start(true);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                if (nodeCache.getCurrentData() != null) {
                    String status = new String(nodeCache.getCurrentData().getData(), "utf-8");
                    JobConfig jobConfig = JSONObject.parseObject(client.getData().forPath(zkJobPath.getJobNamePath()), JobConfig.class);
                    JobExcuteStatusEnum jobExcuteStatusEnum = JobExcuteStatusEnum.valueOf(Integer.parseInt(status));
                    listenerMap.get(jobExcuteStatusEnum.getStatus()).eventHandler(jobConfig, jobExcuteStatusEnum);
                }
            }
        });
    }

    /**
     * 监听任务点的变化情况
     */
    private void addChildWatcher(ZKJobPath zkJobPath) throws Exception {
        final PathChildrenCache childrenCache = new PathChildrenCache(client, zkJobPath.getClientPath(), true);
        childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                JobConfig jobConfig = null;
                if (event.getData() != null) {
                    jobConfig = JSONObject.parseObject(CuratorUtil.getString(event.getData().getData()), JobConfig.class);
                } else {
                    return;
                }
                switch (event.getType()) {
                    case CHILD_ADDED:
                        log.info("CHILD_ADDED: " + jobConfig);
                        Integer jobStatus = JobExcuteStatusEnum.RUN.getStatus();
                        byte[] jobStatusByte = client.getData().forPath(zkJobPath.getJobStatuPath());
                        //job执行状态
                        if (jobStatusByte != null) {
                            jobStatus = CuratorUtil.getInteger(jobStatusByte);
                        }
                        listenerMap.get(JobStatusEnum.CREATE.getStatus()).eventHandler(jobConfig, jobStatus);
                        break;
                    case CHILD_REMOVED:
                        log.info("CHILD_REMOVED: " + jobConfig);
                        listenerMap.get(JobStatusEnum.DELETE.getStatus()).eventHandler(jobConfig);
                        break;
                    case CHILD_UPDATED:
                        listenerMap.get(JobStatusEnum.UPDATE.getStatus()).eventHandler(jobConfig);
                        log.info("CHILD_UPDATED: " + jobConfig);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 添加轮训
     *
     * @param clientId
     */
    public void addLeader(String clientId) throws Exception {
        MasterSelector.getInstance().init("客户端：" + clientId, client, ZKJobPath.LEADER_PATH);
    }
}
