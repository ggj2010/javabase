package com.ggj.java.distributedtask.core.registerycenter.zookeeper;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.ggj.java.distributedtask.core.registerycenter.zookeeper.vo.ZKJobPath;
import com.ggj.java.distributedtask.core.util.Constants;
import com.ggj.java.distributedtask.core.util.CuratorUtil;
import com.ggj.java.distributedtask.core.util.LocalHostService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;

/**
 * @author:gaoguangjin
 * @date 2016/7/27 15:17
 */
@Slf4j
public class MasterSelector extends LeaderSelectorListenerAdapter implements Closeable {
    private volatile boolean isMaster = false;
    private LeaderSelector leaderSelector;
    private Long MASTER_TIME = 1000L * 60 * 1;
    private String name;
    private String ip;
    private CuratorFramework client;
    private ZKJobPath zkJobPath = null;

    private AtomicBoolean init = new AtomicBoolean();
    public AtomicBoolean shutDown = new AtomicBoolean();
    private static AtomicInteger taskNum = new AtomicInteger(0);

    public MasterSelector init(String name, CuratorFramework client, String path) throws Exception {
        if (init.compareAndSet(false, true)) {
            this.client = client;
            this.name = name;
            //leaderSelector 多个客户端监听同一个节点
            this.leaderSelector = new LeaderSelector(client, path, MasterSelectorSigeletonHolder.masterSelector);
            //保证在此实例释放领导权之后还可能获得领导权
            leaderSelector.autoRequeue();
            //启动
            leaderSelector.start();
            this.ip = new LocalHostService().getIp();
            zkJobPath = new ZKJobPath(System.getProperty(Constants.APPID), ip);
            return this;
        } else {
            throw new Exception("不能重复注册选举！！！！");
        }
    }

    private MasterSelector() {
    }

    public static MasterSelector getInstance() {
        return MasterSelectorSigeletonHolder.masterSelector;
    }

    private static class MasterSelectorSigeletonHolder {
        public static MasterSelector masterSelector = new MasterSelector();
    }

    public void increaseJobNum() {
        try {
            byte[] bytes = CuratorUtil.getData(client, zkJobPath.getRunningJobNum());
            //是否存在
            if (bytes == null) {
                CuratorUtil.create(client, CreateMode.EPHEMERAL, zkJobPath.getRunningJobNum(), String.valueOf(1));
            } else {
                CuratorUtil.update(client, zkJobPath.getRunningJobNum(), String.valueOf(taskNum.incrementAndGet()));
            }
        } catch (Exception e) {
            log.error("increaseJobNum error", e);
        }
    }

    public void decreaseJobNum() {
        try {
            byte[] bytes = CuratorUtil.getData(client, zkJobPath.getRunningJobNum());
            //是否存在
            if (bytes == null) {
                return;
            }
            CuratorUtil.update(client, zkJobPath.getRunningJobNum(), String.valueOf(taskNum.decrementAndGet()));
        } catch (Exception e) {
            log.error("decreaseJobNum error", e);
        }
    }

    @Override
    public void close() throws IOException {
        leaderSelector.close();
        log.info(name + "close()");
    }

    /**
     * 实现接口方法，当takeLeadership方法执行完之后 就会释放权限
     *
     * @param curatorFramework
     * @throws Exception
     */
    @Override
    public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
        try {
            log.info("current taskNum:{}", taskNum.get());
            Map<Integer, String> sortMap = CuratorUtil.getMapChildValue(client, zkJobPath.getRunningJob());
            if (sortMap == null) {
                log.info(zkJobPath.getRunningJobNum() + " =>get leadership ");
                isMaster = true;
                Thread.sleep(MASTER_TIME);
                isMaster = false;
            } else {
                String path = ((TreeMap<Integer, String>) sortMap).descendingMap().firstEntry().getValue();
                log.info("currentpath:{},maxloadpath:{},sortMapSize:{},", zkJobPath.getRunningJobNum(), path, sortMap.size());
                //当前负载最高的不是当前机器 就可以选举上立即执行
                if (!path.equals(zkJobPath.getRunningJobNum()) || sortMap.size() == 1) {
                    log.info(zkJobPath.getRunningJobNum() + " =>get leadership");
                    isMaster = true;
                    Thread.sleep(MASTER_TIME);
                    isMaster = false;
                } else {
                    log.info(zkJobPath.getRunningJobNum() + " =>skip leadership");
                    isMaster = false;
                }
            }
        } catch (Exception e) {
            log.error("takeLeadership exception:", e);
        } finally {
            isMaster = false;
            log.info(zkJobPath.getRunningJobNum() + " =>release leadership");
        }
    }

    public boolean isMaster() {
        return isMaster;
    }

}
