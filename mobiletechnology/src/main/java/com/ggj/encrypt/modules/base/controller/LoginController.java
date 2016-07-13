package com.ggj.encrypt.modules.base.controller;

import com.ggj.encrypt.common.constant.GlobalConstant;
import com.ggj.encrypt.modules.base.service.LoginService;
import com.ggj.encrypt.modules.sys.bean.UserInfo;
import com.ggj.encrypt.modules.sys.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/26 15:00
 */
@RestController
@Slf4j
public class LoginController extends BaseController {
	
	@Autowired
	private LoginService loginService;
	@Autowired
	private UserInfoService userInfoService;
	
	/**
	 * 登陆成功返回加密后的token
	 * @param p
	 * @param timestamp
	 * @param appkey
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login/{p}", method = RequestMethod.POST)
	public String login(@PathVariable("p") String p, @RequestHeader(required = true, name = GlobalConstant.TIMESTAMP) String timestamp,
		@RequestHeader(required = true, name = GlobalConstant.APPKEY) String appkey, HttpServletResponse response) throws Exception {
		UserInfo userInfo = loginService.validateAndGetUserInfo(p, timestamp, appkey);
		UserInfo dataBaseUserInfo = userInfoService.getLoginUserInfo(userInfo);
		return loginService.getSuccessLoginResult(dataBaseUserInfo, appkey, response);
	}
	
}
