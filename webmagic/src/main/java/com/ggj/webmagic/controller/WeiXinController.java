package com.ggj.webmagic.controller;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.ggj.webmagic.autoconfiguration.WeiXinConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * 微信扫码登录
 * @author:gaoguangjin
 * @date 2016/9/23 15:31
 */
@Controller
@Slf4j
public class WeiXinController {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private WeiXinConfig weiXinConfig;
	
	/**
	 * 如果在开放平台绑定网站信息就可以使用
	 *https://open.weixin.qq.com/connect/qrconnect 这个方式授权登录了
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/")
	public String weiXin(Model model,HttpServletRequest request) throws Exception {
		String token = UUID.randomUUID().toString();
		// ngrok 生产的localhost映射域名
		String targetUrl = weiXinConfig.getAuthCallBackUrl() + token;
		targetUrl = URLEncoder.encode(targetUrl, "utf-8");
		String url = String.format(weiXinConfig.getAuthorizeUrl(), weiXinConfig.getAppid(), targetUrl);
		model.addAttribute("token", token);
		model.addAttribute("url", url);
		String websocketUrl = request.getServerName()  + request.getContextPath() ;
		model.addAttribute("websocketUrl",websocketUrl);
		return "weixin/login";
	}
	
	@RequestMapping("/weixin/auth")
	public String weiXinAuth(Model model, String code, String token) throws Exception {
		String url = String.format(weiXinConfig.getAccessTokenUrl(), weiXinConfig.getAppid(), weiXinConfig.getSecret(), code);
		String result = Request.Get(url).execute().returnContent().asString();
		if (StringUtils.isNotEmpty(result)) {
			JSONObject jsonObject = JSONObject.parseObject(result);
			if (!jsonObject.containsKey("errcode")) {
				String openid = jsonObject.getString("openid");
				String accessToken = jsonObject.getString("access_token");
				String userInfoUrl = String.format(weiXinConfig.getUserinfoUrl(), accessToken, openid);
				String userInfoResult = Request.Get(userInfoUrl).execute().returnContent()
						.asString(Charset.forName("utf-8"));
				log.info("userInfoResult=" + userInfoResult);
				redisTemplate.opsForValue().set(token, "1",2, TimeUnit.MINUTES);
				redisTemplate.opsForValue().set(token + "info", userInfoResult, 30, TimeUnit.MINUTES);
				model.addAttribute("result","登录成功！");
			}else{
				model.addAttribute("result","登录失败！");
			}
		}
//		return "weixin/loginResult";
		return "redirect:https://ggj2010.bid/tiebaimage/tiebaimage";
	}
	
	@RequestMapping("/weixin/center")
	public String weiXinAuth(Model model, String token) throws Exception {
		String userInfo = redisTemplate.opsForValue().get(token + "info");
		if(StringUtils.isNotEmpty(userInfo)) {
			JSONObject jsonObject = JSONObject.parseObject(userInfo);
			model.addAttribute("name", jsonObject.getString("nickname"));
			model.addAttribute("headimgurl", jsonObject.getString("headimgurl"));
			model.addAttribute("country", jsonObject.getString("country"));
			model.addAttribute("province", jsonObject.getString("province"));
			model.addAttribute("city", jsonObject.getString("city"));
			model.addAttribute("sex", jsonObject.getString("sex").equals("1")?"男":"女");
		}
        return "weixin/userInfo";
	}
}
