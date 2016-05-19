package com.ggj.encrypt.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ggj.encrypt.common.exception.BizException;
import com.ggj.encrypt.common.persistence.ApiUserToken;
import com.ggj.encrypt.common.utils.redis.RedisDaoTemplate;
import com.ggj.encrypt.common.utils.redis.callback.RedisCallback;
import com.ggj.encrypt.configuration.RedisKeyConfiguration;
import com.ggj.encrypt.configuration.ResultCodeConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Date;

/**
 * @author:gaoguangjin
 * @Description:生成手机端token工具类
 * @Email:335424093@qq.com
 * @Date 2016/4/27 16:56
 */
@Slf4j
@Component
public class TokenHelper {

    @Autowired
    private RedisDaoTemplate redisDaoTemplate;

    @Autowired
    private RedisKeyConfiguration redisKeyConfiguration;
    @Autowired
    private ResultCodeConfiguration resultCodeConfiguration;


    private final int TIME_TO_LIVE = 30 * 24 * 60 * 60 ;// 30天
    public  ApiUserToken createToken(Long userID,String appkey) throws Exception {
       return createToken(userID,appkey,TIME_TO_LIVE);
    }
    public  ApiUserToken createToken(Long userID,String appkey,long days) throws Exception {
        ApiUserToken apiUserToken=new ApiUserToken();
        apiUserToken.setAppkey(appkey);
        apiUserToken.setToken(ApiTools.getRandomUUID());
        apiUserToken.setCreateTime(new Date());
        apiUserToken.setLastVisitTime(apiUserToken.getCreateTime());
        apiUserToken.setUserId(userID);
        apiUserToken.setDelayTime(days/24 * 60 * 60 * 1000);
        //保存token 到缓存
        saveTokeToRedis(apiUserToken,userID);
        return apiUserToken;
    }

    /**
     * 保存token 到redis
     * @param apiUserToken
     * @param userID
     * @throws Exception
     */
    private void saveTokeToRedis(ApiUserToken apiUserToken,Long userID) throws Exception {
        redisDaoTemplate.execute(new RedisCallback<String>(){
            public String doInRedis(Jedis jedis) throws Exception {
             String key=redisKeyConfiguration.getUserToken()+userID;
                jedis.setex(key, TIME_TO_LIVE, JSON.toJSONString(apiUserToken));
                return null;
            }
        });
    }

    /**
     * 获取登陆token
     * @param userID
     * @return
     * @throws Exception
     */
    public ApiUserToken getUserToken(Long userID)throws Exception{
        return redisDaoTemplate.execute(new RedisCallback<ApiUserToken>(){
            public ApiUserToken doInRedis(Jedis jedis) throws Exception {
                String key=redisKeyConfiguration.getUserToken()+userID;
                log.info("loginkey="+key);
                String userTokenJson=jedis.get(key);
                if(!StringUtils.isEmpty(userTokenJson)){
                   return JSON.parseObject(userTokenJson,ApiUserToken.class);
                }
                throw new BizException(resultCodeConfiguration.getTokenErrorCode(),resultCodeConfiguration.getTokenErrorCodeMsg());
            }
        });
    }
}
