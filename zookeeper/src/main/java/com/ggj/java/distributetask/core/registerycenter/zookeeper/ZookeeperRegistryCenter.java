package com.ggj.java.distributetask.core.registerycenter.zookeeper;

import com.alibaba.fastjson.JSONObject;
import com.ggj.java.distributetask.core.job.JobConfig;
import com.ggj.java.distributetask.core.job.JobStatu;
import com.ggj.java.distributetask.core.registerycenter.listener.ZookperChildChangeListener;
import com.ggj.java.distributetask.core.registerycenter.listener.ZookperDataChangeListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author:gaoguangjin
 * @date 2016/9/1 17:21
 */
@Slf4j
public class ZookeeperRegistryCenter implements RegistryCenter {
    @Getter
    private CuratorFramework client;

    public void init(ZKConfig zkConfig) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(zkConfig.getConnectString(), retryPolicy);
        client.start();
        client.usingNamespace(zkConfig.getNameSpace());
    }

    @Override
    public void register(JobConfig jobConfig) {
        ZKPath zkPath = new ZKPath(jobConfig.getClientId(),jobConfig.getIp(),jobConfig.getJobName());
        try {
            create(zkPath.getJobDataPath(), JSONObject.toJSONString(zkPath));
            create(CreateMode.EPHEMERAL,zkPath.getJobIpDataPath(), jobConfig.getIp());
            create(zkPath.getJobStatuPath(), JobStatu.RUN.getStatus()+"");
            addChildWatcher(zkPath,jobConfig);
            addStatusWatcher(zkPath,jobConfig);
        } catch (Exception e) {
          log.error("注册失败！"+e.getLocalizedMessage());
        }
    }

    private void addStatusWatcher(ZKPath zkPath, JobConfig jobConfig) throws Exception {
        final NodeCache nodeCache = new NodeCache(client, zkPath.getJobStatuPath(), false);
        nodeCache.start(true);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
               String data=new String(nodeCache.getCurrentData().getData(),"utf-8");
                JobStatu jobStatu = JobStatu.valueOf(Integer.parseInt(data));
                new ZookperDataChangeListener().eventHandler(jobConfig,jobStatu);
            }
        });

    }

    /**
     * 监听子节点的变化情况
     */
    private void addChildWatcher(ZKPath zkPath, JobConfig jobConfig) throws Exception {
        final PathChildrenCache childrenCache = new PathChildrenCache(client, zkPath.getJobIpPath(), true);
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                switch(event.getType()) {
                    case CHILD_ADDED:
                        log.info("CHILD_ADDED: " + event.getData().getPath());
                        break;
                    case CHILD_REMOVED:
                        log.info("CHILD_REMOVED: " + event.getData().getPath());
                            new ZookperChildChangeListener().eventHandler(jobConfig);
                        break;
                    case CHILD_UPDATED:
                        log.info("CHILD_UPDATED: " + event.getData().getPath());
                        break;
                    default:
                        break;
                }
            }
        });


    }


    private  boolean checkExists(String path) throws Exception {
        return client.checkExists().forPath(path) == null ? false : true;
    }


    public void create(CreateMode createMode,String path,String data) throws Exception {
        if(!checkExists(path)) {
            client.create().creatingParentsIfNeeded().withMode(createMode).forPath(path, data.getBytes());
        }
    }

    /**
     * 默认 节点是PERSISTENT
     * @param path
     * @param data
     * @throws Exception
     */
    public void create(String path,String data) throws Exception {
        if(!checkExists(path)) {
            client.create().creatingParentsIfNeeded().forPath(path, data.getBytes());
        }
    }

    /**
     * 添加轮训
     * @param clientId
     */
    public void addLeader(String clientId) {
        new MasterSelector(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date())+"客户端：" + clientId, client, ZKPath.LEADER_PATH);
    }
}
