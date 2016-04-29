package com.ggj.encrypt.modules.sys.service;

import com.ggj.encrypt.common.exception.BizException;
import com.ggj.encrypt.common.utils.DesUtil;
import com.ggj.encrypt.common.utils.MD5Util;
import com.ggj.encrypt.configuration.ResultCodeConfiguration;
import com.ggj.encrypt.modules.sys.bean.UserInfo;
import com.ggj.encrypt.modules.sys.dao.UserDao;
import com.ggj.encrypt.security.LoginSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/28 10:35
 */
@Service
public class UserInfoService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private LoginSecurity loginSecurity;
	@Autowired
	private ResultCodeConfiguration resultCodeConfiguration;

	public UserInfo getLoginUserInfo(UserInfo userInfo) throws Exception {
		loginSecurity.checkLoginErrorTimes(userInfo);
		// TODO: 2016/4/28 模拟调用数据库 根据用户名查询到用户记录，返回用户信息
        UserInfo dbUserInfo=userDao.getUserInfo(userInfo);
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
}
