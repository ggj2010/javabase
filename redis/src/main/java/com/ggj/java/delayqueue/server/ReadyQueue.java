package com.ggj.java.delayqueue.server;

import com.alibaba.fastjson.JSONObject;
import com.ggj.java.delayqueue.server.bean.JobDetail;
import com.ggj.java.delayqueue.server.common.Constants;
import com.ggj.java.delayqueue.server.redis.RedisTemplate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Ready Queue存放处于Ready状态的Job（这里只存放Job Id），以供消费程序消费
 *
 * @author gaoguangjin
 */
public class ReadyQueue {
    private RedisTemplate redisTemplate;

    public ReadyQueue(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void add(List<JobDetail> dealyJobList) throws Exception {
        if (CollectionUtils.isEmpty(dealyJobList)) {
            return;
        }
        redisTemplate.exceute((jedis) -> {
            for (JobDetail jobDetail : dealyJobList) {
                // 可以优化 piple、或者group put
                String key = Constants.JOB_READY_TOPIC_KEY + jobDetail.getTopic();
                jedis.lpush(key, jobDetail.getJobId());
            }
            return null;
        });
    }

    public JobDetail pop(String topic) throws Exception {
        if (StringUtils.isEmpty(topic)) {
            return null;
        }
        return redisTemplate.exceute((jedis) -> {
            String key = Constants.JOB_READY_TOPIC_KEY + topic;
            String jobId = jedis.lpop(key);
            if (StringUtils.isEmpty(jobId)) {
                return null;
            }
            String jobDetailJson = jedis.get(Constants.JOB_DETAIL_KEY + jobId);
            if (StringUtils.isEmpty(jobDetailJson)) {
                return null;
            }
            return JSONObject.parseObject(jobDetailJson, JobDetail.class);
        });
    }

    public List<JobDetail> popList(String topic) throws Exception {
        if (StringUtils.isEmpty(topic)) {
            return null;
        }
        return redisTemplate.exceute((jedis) -> {
            String key = Constants.JOB_READY_TOPIC_KEY + topic;
            List<String> keyList = jedis.lrange(key, 0, -1);
            if (CollectionUtils.isNotEmpty(keyList)) {
                List<JobDetail> jobDetailList = new ArrayList<>();
                for (String jobId : keyList) {
                    String jobDetailJson = jedis.get(Constants.JOB_DETAIL_KEY + jobId);
                    jobDetailList.add(JSONObject.parseObject(jobDetailJson, JobDetail.class));
                }
                jedis.ltrim(key, keyList.size(), -1);
                return jobDetailList;
            } else {
                return null;
            }
        });
    }
}
