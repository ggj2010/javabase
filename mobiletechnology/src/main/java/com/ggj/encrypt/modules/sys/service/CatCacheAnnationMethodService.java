package com.ggj.encrypt.modules.sys.service;

import com.alibaba.fastjson.JSON;
import com.ggj.encrypt.common.annation.catclient.method.CatMethodCache;
import com.ggj.encrypt.common.utils.redis.RedisDaoTemplate;
import com.ggj.encrypt.common.utils.redis.callback.RedisCallback;
import com.ggj.encrypt.configuration.RedisKeyConfiguration;
import com.ggj.encrypt.modules.sys.bean.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;


/**
 * @author:gaoguangjin
 * @date 2016/7/12 19:32
 */
@Service

public class CatCacheAnnationMethodService {
    @Autowired
    private RedisDaoTemplate redisDaoTemplate;

    /**
     * 如果某个方法要执行redis操作 而且同时用cat对redis进行监控， 那么这个方法必须要有一个参数key 是redis的key
     * @param key
     * @return
     */
    @CatMethodCache(name = "get")
    public UserInfo getUserInfoFromRedis(String key) throws Exception {
        if (StringUtils.isEmpty(key))
            return null;
        return redisDaoTemplate.execute(new RedisCallback<UserInfo>() {
            public UserInfo doInRedis(Jedis jedis) throws Exception {
                String userInfoJson = jedis.get(key);
                if (userInfoJson != null)
                    return JSON.parseObject(userInfoJson, UserInfo.class);
                return null;
            }
        });
    }
}
