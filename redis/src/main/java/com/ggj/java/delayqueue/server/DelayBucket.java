package com.ggj.java.delayqueue.server;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ggj.java.delayqueue.server.bean.JobDetail;
import com.ggj.java.delayqueue.server.common.Constants;
import com.ggj.java.delayqueue.server.redis.RedisTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Delay Bucket是一个有序队列。
 * <p>
 * Delay Bucket是一组以时间为维度的有序队列，用来存放所有需要延迟的／已经被reserve的Job（这里只存放Job Id）
 *
 * @author gaoguangjin
 */
@Slf4j
public class DelayBucket {
    /**
     * 分同是为了提高查询效率
     */
    private Integer bucketSize;
    private RedisTemplate redisTemplate;

    public DelayBucket(Integer bucketSize, RedisTemplate redisTemplate) {
        this.bucketSize = bucketSize;
        this.redisTemplate = redisTemplate;
    }

    public void addJob(JobDetail jobDetail) throws Exception {
        long key = System.currentTimeMillis() % bucketSize;
        redisTemplate.exceute((jedis) -> {
            jedis.zadd(Constants.JOB_BUCKET_KEY + key, jobDetail.getDealy(), jobDetail.getJobId());
            return null;
        });
    }


    public List<JobDetail> getAllDealyJob() throws Exception {
        return redisTemplate.exceute((jedis) -> {
            List<JobDetail> jobDetailList = new ArrayList<>();
            List<String> idList = new ArrayList<>();
            for (int i = 0; i < bucketSize; i++) {
                Set<String> dealyJobIdSet = jedis.zrangeByScore(Constants.JOB_BUCKET_KEY + i, 0, System.currentTimeMillis());
                if (CollectionUtils.isEmpty(dealyJobIdSet)) {
                    continue;
                }
                String[] deleteArray = dealyJobIdSet.toArray(new String[dealyJobIdSet.size()]);
                for (String id : dealyJobIdSet) {
                    idList.add(Constants.JOB_DETAIL_KEY + id);
                }
                //清除，需要考虑回滚问题。
                jedis.zrem(Constants.JOB_BUCKET_KEY + i, deleteArray);
            }
            if (CollectionUtils.isEmpty(idList)) {
                return null;
            }
            String[] array = idList.toArray(new String[idList.size()]);
            List<String> resultList = jedis.mget(array);
            if (CollectionUtils.isEmpty(resultList)&&resultList.size()==0) {
                return null;
            }
            for (String str : resultList) {
                jobDetailList.add(JSONObject.parseObject(str, JobDetail.class));
            }
            return jobDetailList;
        });
    }
}
