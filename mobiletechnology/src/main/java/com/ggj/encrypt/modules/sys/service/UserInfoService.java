package com.ggj.encrypt.modules.sys.service;

import com.alibaba.fastjson.JSON;
import com.ggj.encrypt.common.exception.BizException;
import com.ggj.encrypt.common.utils.DesUtil;
import com.ggj.encrypt.common.utils.MD5Util;
import com.ggj.encrypt.common.utils.redis.RedisDaoTemplate;
import com.ggj.encrypt.common.utils.redis.callback.RedisCallback;
import com.ggj.encrypt.configuration.RedisKeyConfiguration;
import com.ggj.encrypt.configuration.ResultCodeConfiguration;
import com.ggj.encrypt.modules.sys.bean.UserInfo;
import com.ggj.encrypt.modules.sys.dao.UserInfoMapper;
import com.ggj.encrypt.security.LoginSecurity;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/28 10:35
 */
@Service
@Transactional(readOnly = true)
public class UserInfoService {
	@Autowired
	private UserInfoMapper userInfoMapperDao;
	@Autowired
	private LoginSecurity loginSecurity;
	@Autowired
	private ResultCodeConfiguration resultCodeConfiguration;
	@Autowired
	private RedisDaoTemplate redisDaoTemplate;
	@Autowired
	private RedisKeyConfiguration redisKeyConfiguration;

	public UserInfo getLoginUserInfo(UserInfo userInfo) throws Exception {
		loginSecurity.checkLoginErrorTimes(userInfo);
        UserInfo dbUserInfo=getUserInfo(userInfo);
		if (dbUserInfo == null) {
			// 登陆名不存在异常！！！
			throw new BizException(resultCodeConfiguration.getLoginNameWrong(), resultCodeConfiguration.getLoginNameWrongMsg());
		}
		String salt= DesUtil.encrypt(userInfo.getLoginName());
		//加salt MD5
		String password=MD5Util.md5Encode(MD5Util.md5Encode(userInfo.getPassword())+salt);
		if (password.equals(dbUserInfo.getPassword())) {
			return dbUserInfo;
		} else {
			loginSecurity.addLoginErrorTimes(userInfo);
			throw new BizException(resultCodeConfiguration.getPasswordWrong(), resultCodeConfiguration.getPasswordWrongMsg());
		}
	}


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

	/**
	 * 保存到数据库，同事存到缓存
	 * @param userInfo
	 * @throws Exception
     */
	@Transactional(readOnly = false)
	public void addUserInfo(UserInfo userInfo) throws Exception {
		userInfoMapperDao.insert(userInfo);
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

	public UserInfo getUserInfo(Integer id) {
		return userInfoMapperDao.selectByPrimaryKey(id);
	}

	public List<UserInfo> findAll() {
		return  userInfoMapperDao.findList(null);
	}
}
