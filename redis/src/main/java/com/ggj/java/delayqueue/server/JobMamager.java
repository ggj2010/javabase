package com.ggj.java.delayqueue.server;

import com.ggj.java.delayqueue.server.bean.JobDetail;
import com.ggj.java.delayqueue.server.common.Constants;
import com.ggj.java.delayqueue.server.redis.RedisTemplate;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author gaoguangjin
 */
@Slf4j
public class JobMamager {
    @Getter
    private ReadyQueue readyQueue;
    private JobPool jobPool;


    public static JobMamager getInstance() {
        return SingletonHolder.jobMamager;
    }

    private JobMamager() {
        RedisTemplate redisTemplate = RedisTemplate.getRedisTemplate();
        DelayBucket delayBucket = new DelayBucket(Constants.MAX_BUCKET_SIZE, redisTemplate);
        jobPool = new JobPool(redisTemplate, delayBucket);
        readyQueue = new ReadyQueue(redisTemplate);
        new Thread(new JobTimer(delayBucket, readyQueue, redisTemplate)).start();
    }

    public List<JobDetail> popJobList(String topic) {
        try {
            return readyQueue.popList(topic);
        } catch (Exception e) {
            log.error("",e);
        }
        return null;
    }

    private static class SingletonHolder {
        private static final JobMamager jobMamager = new JobMamager();
    }

    public void putJob(JobDetail jobDetail) {
        try {
            jobPool.putJobDetail(jobDetail);
        } catch (Exception e) {
            log.error("",e);
        }
    }

    public JobDetail popJob(String topic) {
        try {
            return readyQueue.pop(topic);
        } catch (Exception e) {
           log.error("",e);
        }
        return null;
    }
}
