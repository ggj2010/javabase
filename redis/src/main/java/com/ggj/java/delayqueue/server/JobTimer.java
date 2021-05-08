package com.ggj.java.delayqueue.server;


import com.ggj.java.delayqueue.server.bean.JobDetail;
import com.ggj.java.delayqueue.server.common.Constants;
import com.ggj.java.delayqueue.server.redis.RedisTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.UUID;

/**
 * Timer负责实时扫描各个Bucket，并将delay时间大于等于当前时间的Job放入到对应的Ready Queue。
 *
 * @author gaoguangjin
 */
@Slf4j
public class JobTimer implements Runnable {
    private DelayBucket delayBucket;
    private ReadyQueue readyQueue;
    private RedisTemplate redisTemplate;

    public JobTimer(DelayBucket delayBucket, ReadyQueue readyQueue, RedisTemplate redisTemplate) {
        this.delayBucket = delayBucket;
        this.readyQueue = readyQueue;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void run() {
        while (true) {
            String randomId = UUID.randomUUID().toString();
            try {
                // 分布式锁
                if (!getLock(randomId)) {
                    continue;
                }
                List<JobDetail> dealyJobList = delayBucket.getAllDealyJob();
                if (CollectionUtils.isEmpty(dealyJobList)) {
                    continue;
                }
                readyQueue.add(dealyJobList);
                Thread.sleep(1);
            } catch (Exception e) {
                log.error("", e);
            } finally {
                try {
                    releaseLock(randomId);
                } catch (Exception e) {
                    log.error("relase lock error", e);
                }
            }
        }
    }

    private void releaseLock(String randomId) throws Exception {
        redisTemplate.exceute((jedis) -> {
            String value = jedis.get(Constants.JOB_LOCK_KEY);
            if (StringUtils.isNotEmpty(value) && value.equalsIgnoreCase(randomId)) {
                jedis.del(Constants.JOB_LOCK_KEY);
            }
            return false;
        });
    }

    private boolean getLock(String randomId) throws Exception {
        return redisTemplate.exceute((jedis) -> {
            String value = jedis.setex(Constants.JOB_LOCK_KEY, Constants.LOCK_TIME, randomId);
            if (StringUtils.isNotEmpty(value) && value.equalsIgnoreCase(Constants.OK)) {
                return true;
            }
            return false;
        });
    }
}
