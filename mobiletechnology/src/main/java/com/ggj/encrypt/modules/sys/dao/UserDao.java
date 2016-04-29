package com.ggj.encrypt.modules.sys.dao;

import com.alibaba.fastjson.JSON;
import com.ggj.encrypt.common.utils.DesUtil;
import com.ggj.encrypt.common.utils.MD5Util;
import com.ggj.encrypt.common.utils.redis.RedisDaoTemplate;
import com.ggj.encrypt.common.utils.redis.callback.RedisCallback;
import com.ggj.encrypt.configuration.RedisKeyConfiguration;
import com.ggj.encrypt.modules.sys.bean.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * @author:gaoguangjin
 * @Description:这里为了方便 直接调用redis
 * @Email:335424093@qq.com
 * @Date 2016/4/28 11:39
 */
@Component
public class UserDao {
	@Autowired
	private RedisDaoTemplate redisDaoTemplate;
	@Autowired
	private RedisKeyConfiguration redisKeyConfiguration;
	
	public UserInfo getUserInfo(UserInfo userInfo) throws Exception {
		if (StringUtils.isEmpty(userInfo.getLoginName()))
			return null;
		return redisDaoTemplate.execute(new RedisCallback<UserInfo>() {
			public UserInfo doInRedis(Jedis jedis) throws Exception {
				String key = redisKeyConfiguration.getUserInfoLoginName() + userInfo.getLoginName();
				String userInfoJson = jedis.get(key);
				if (userInfoJson != null)
					return JSON.parseObject(userInfoJson, UserInfo.class);
				return null;
			}
		});
	}
	
	public void addUserInfo(UserInfo userInfo) throws Exception {
		String salt = DesUtil.encrypt(userInfo.getLoginName());
		// 加salt MD5
		userInfo.setPassword(MD5Util.md5Encode(MD5Util.md5Encode(userInfo.getPassword()) + salt));
		redisDaoTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(Jedis jedis) throws Exception {
				String key = redisKeyConfiguration.getUserInfoLoginName() + userInfo.getLoginName();
				String userInfoJson = jedis.set(key, JSON.toJSONString(userInfo));
				return null;
			}
		});
	}
}
