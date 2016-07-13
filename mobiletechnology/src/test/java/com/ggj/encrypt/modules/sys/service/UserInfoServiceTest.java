package com.ggj.encrypt.modules.sys.service;


import com.ggj.encrypt.modules.BaseTest;
import com.ggj.encrypt.modules.sys.bean.UserInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author:gaoguangjin
 * @date 2016/7/8 17:19
 */
public class UserInfoServiceTest extends BaseTest {
    @Autowired
    private UserInfoService  userInfoService;
	
	public final static String LOGINNAME = "gaoguangjin2";
	
	public final static String PASSWORD = "qazqaz2";
	
	@Test
	public void getLoginUserInfo() throws Exception {
		
	}
	
	@Test
	public void getUserInfo() throws Exception {
		
	}
	
	@Test
	public void addUserInfo() throws Exception {
		UserInfo userInfo = new UserInfo();
		userInfo.setId(2l);
		userInfo.setLoginName(LOGINNAME);
		userInfo.setPassword(PASSWORD);
		userInfoService.addUserInfo(userInfo);
	}
}