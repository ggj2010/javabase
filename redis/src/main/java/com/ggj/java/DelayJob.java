package com.ggj.java;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.Set;

/**
 * 非单机需要考虑任务重复执行问题+分布式锁
 * redis 实现延时job
 * @author gaoguangjin
 */
@Slf4j
public class DelayJob {
    public static void main(String[] args) throws InterruptedException {
        Jedis jedis = new Jedis("localhost");
        //jedis.auth("123");
        String key="dealtask";
        long beginTime=System.currentTimeMillis();
        log.info("beginTime={}",beginTime);
        jedis.zadd(key,beginTime+1000,new Task("1.1","延迟1秒").toJson());
        jedis.zadd(key,beginTime+1000,new Task("1.2","延迟1秒").toJson());
        jedis.zadd(key,beginTime+1000,new Task("1.3","延迟1秒").toJson());
        jedis.zadd(key,beginTime+3000,new Task("2","延迟3秒").toJson());
        jedis.zadd(key,beginTime+2000,new Task("3","延迟2秒").toJson());
        jedis.zadd(key,beginTime+5000,new Task("4","延迟5秒").toJson());

        while (true){
            Set<Tuple> set = jedis.zrangeByScoreWithScores(key, System.currentTimeMillis(), System.currentTimeMillis()+1000);
            if(set==null||set.isEmpty()){
                Thread.sleep(500);
                continue;
            }
            for (Tuple tuple : set) {
                //取出来第一个任务
                String value = tuple.getElement();
                int score = (int) tuple.getScore();
                if (System.currentTimeMillis() >= score) {
                    jedis.zrem(key, value);
                    log.info("excute job {}",value);
                }
            }
        }
    }

    @Data
    static
    class Task{
        private String id;
        private String name;

        public Task(String id, String name) {
            this.id = id;
            this.name = name;
        }
        public String toJson(){
            return JSONObject.toJSONString(this);
        }

    }
}
