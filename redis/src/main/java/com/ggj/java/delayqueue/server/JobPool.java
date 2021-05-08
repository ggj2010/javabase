package com.ggj.java.delayqueue.server;

import com.alibaba.fastjson.JSONObject;
import com.ggj.java.delayqueue.server.bean.JobDetail;
import com.ggj.java.delayqueue.server.common.Constants;
import com.ggj.java.delayqueue.server.redis.RedisTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * Job Poll存放的Job元信息，只需要K/V形式的结构即可
 *
 * @author gaoguangjin
 */
@Slf4j
public class JobPool {
    private RedisTemplate redisTemplate;
    private DelayBucket delayBucket;

    public JobPool(RedisTemplate redisTemplate, DelayBucket delayBucket) {
        this.redisTemplate = redisTemplate;
        this.delayBucket = delayBucket;
    }

    public boolean putJobDetail(JobDetail jobDetail) throws Exception {
        redisTemplate.exceute((jedis) -> {
            jedis.set(Constants.JOB_DETAIL_KEY + jobDetail.getJobId(), JSONObject.toJSONString(jobDetail));
            delayBucket.addJob(jobDetail);
            return null;
        });
        return true;
    }

    public JobDetail getJobDetail(String id) throws Exception {
        return redisTemplate.exceute(jedis -> {
            String value = jedis.get(Constants.JOB_DETAIL_KEY + id);
            if (StringUtils.isNotEmpty(value)) {
                return JSONObject.parseObject(value, JobDetail.class);
            }
            return null;
        });
    }
}
