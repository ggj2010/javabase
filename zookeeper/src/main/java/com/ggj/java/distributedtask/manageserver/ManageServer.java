package com.ggj.java.distributedtask.manageserver;

import com.ggj.java.distributedtask.core.job.enums.JobExcuteStatusEnum;
import com.ggj.java.distributedtask.core.job.enums.JobStatusEnum;
import com.ggj.java.distributedtask.core.registerycenter.zookeeper.ZookeeperRegistryCenter;
import com.ggj.java.distributedtask.core.registerycenter.zookeeper.vo.ZKConfig;
import com.ggj.java.distributedtask.core.registerycenter.zookeeper.vo.ZKJobPath;
import com.ggj.java.distributedtask.core.util.Constants;
import com.ggj.java.distributedtask.core.util.CuratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

/**
 * 管理中心
 *
 * @author gaoguangjin
 */
@Slf4j
public class ManageServer {
    private static final String CLIENT_ID = "seg-homework-api";

    public static final String CONNECT_URL = "localhost:2181";
    public static final String NAME_SPACE = "distributeJob";
    public static final String TEST_JOB_NAME = "clientOne";

    public static void main(String[] args) throws IOException {
        System.setProperty(Constants.APPID, CLIENT_ID);
        ZookeeperRegistryCenter zookeeperRegistryCenter = ZookeeperRegistryCenter.getInstance();
        zookeeperRegistryCenter.init(new ZKConfig(CONNECT_URL, NAME_SPACE));
        init(zookeeperRegistryCenter.getClient());
    }

    private static void init(CuratorFramework client) throws IOException {
        //显示所有运行中的job
        listAllJobInfo(client);
        System.in.read();

        log.info("暂停"+TEST_JOB_NAME);
        //暂停
        pause(client);
        System.in.read();

        //启动
        log.info("启动"+TEST_JOB_NAME);
        runJob(client);
        System.in.read();

        //停止
        log.info("停止"+TEST_JOB_NAME);
        stopJob(client);
        System.in.read();

        //启动
        log.info("启动"+TEST_JOB_NAME);
        runJob(client);
        System.in.read();
    }

    private static void stopJob(CuratorFramework client) {
        ZKJobPath zkChildJobPath = new ZKJobPath(CLIENT_ID, null, TEST_JOB_NAME);
        try {
            CuratorUtil.update(client, zkChildJobPath.getJobStatuPath(), String.valueOf(JobExcuteStatusEnum.STOP.getStatus()));
        } catch (Exception e) {
            log.error("pastopJobuse job error", e);
        }

    }

    private static void runJob(CuratorFramework client) {
        ZKJobPath zkChildJobPath = new ZKJobPath(CLIENT_ID, null, TEST_JOB_NAME);
        try {
            CuratorUtil.update(client, zkChildJobPath.getJobStatuPath(), String.valueOf(JobExcuteStatusEnum.RUN.getStatus()));
        } catch (Exception e) {
            log.error("runJob job error", e);
        }
    }

    /**
     * 暂停job
     *
     * @param client
     */
    private static void pause(CuratorFramework client) {
        ZKJobPath zkChildJobPath = new ZKJobPath(CLIENT_ID, null, TEST_JOB_NAME);
        try {
            CuratorUtil.update(client, zkChildJobPath.getJobStatuPath(), String.valueOf(JobExcuteStatusEnum.PAUSE.getStatus()));
        } catch (Exception e) {
            log.error("pause job error", e);
        }
    }

    private static void listAllJobInfo(CuratorFramework client) {
        ZKJobPath zkJobPath = new ZKJobPath(CLIENT_ID, null);
        try {
            List<String> childPath = CuratorUtil.getChildPath(client, zkJobPath.getJobRootPath());
            if(CollectionUtils.isEmpty(childPath)){
                return;
            }
            for (String path : childPath) {
                ZKJobPath zkChildJobPath = new ZKJobPath(CLIENT_ID, null, path);
                Integer jobStatus = CuratorUtil.getInteger(CuratorUtil.getData(client, zkChildJobPath.getJobStatuPath()));
                log.info("path:{},jobexcutestatus:{}", zkChildJobPath.getJobStatuPath(), JobExcuteStatusEnum.valueOf(jobStatus).getName());
            }
        } catch (Exception e) {
            log.error("get child job error", e);
        }
    }
}
