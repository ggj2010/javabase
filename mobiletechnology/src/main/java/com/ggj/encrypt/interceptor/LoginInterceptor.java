package com.ggj.encrypt.interceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ggj.encrypt.common.constant.GlobalConstant;
import org.springframework.core.NamedThreadLocal;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.ggj.encrypt.common.exception.BizException;
import com.ggj.encrypt.common.utils.ApiTools;
import com.ggj.encrypt.common.utils.DateUtils;
import com.ggj.encrypt.common.utils.SpringContextHolder;
import com.ggj.encrypt.common.utils.kafka.KafkaClientUtil;
import com.ggj.encrypt.configuration.ResultCodeConfiguration;
import com.ggj.encrypt.security.LoginSecurity;

import lombok.extern.slf4j.Slf4j;

/**
 * @author:gaoguangjin
 * @Description:自定义登陆拦截
 * @Email:335424093@qq.com
 * @Date 2016/4/26 15:04
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
	private static final String DEFAULT_CHARSET = "UTF-8";
	private static final String DEFAULT_CONTENT_TYPE_NAME = "content-type";
	private static final String DEFAULT_CONTENT_TYPE_VALUE = "application/json;charset=UTF-8";
	private static final ThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("ThreadLocal StartTime");
	private LoginSecurity loginSecurity = SpringContextHolder.getBean(LoginSecurity.class);
	private ResultCodeConfiguration resultCodeConfiguration = SpringContextHolder.getBean(ResultCodeConfiguration.class);
	private KafkaClientUtil kafkaClientUtil = SpringContextHolder.getBean(KafkaClientUtil.class);

	/**
	 * 检验用户是否登陆，没有登录直接返回
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		response.setCharacterEncoding(DEFAULT_CHARSET);
		response.setHeader(DEFAULT_CONTENT_TYPE_NAME, DEFAULT_CONTENT_TYPE_VALUE);
		long beginTime = System.currentTimeMillis();// 1、开始时间
		startTimeThreadLocal.set(beginTime); // 线程绑定变量（该数据只有当前请求的线程可见）
		try {
			loginSecurity.checkIsLoginAndValidSgnature(request);
			return true;
		} catch (BizException e) {
			response.getOutputStream().write(e.getReturnRestult().toJSONString().getBytes(DEFAULT_CHARSET));
		} catch (Exception e) {
			response.getOutputStream().write(resultCodeConfiguration.getErrorResultCode().getBytes(DEFAULT_CHARSET));
		}
		return false;
	}
	
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		try {
			saveLogToKafka(request);
		} catch (Exception e) {
			log.error("保存日志到kafka失败！" + e.getLocalizedMessage());
		}
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		long beginTime = startTimeThreadLocal.get();
		long endTime = System.currentTimeMillis();
		long execTime = endTime - beginTime;
		// 大于5s,打印一下警告日志
		if (execTime > 200) {
			log.info(request.getRequestURI() + " 访问时间过长, 耗费:" + execTime + " ms");
		}
	}

	/**
	 * 异步将访问信息丢到卡夫卡
	 * @param request
	 * @throws Exception
     */
	private void saveLogToKafka(HttpServletRequest request) throws Exception {
		// TODO 按照约定好的格式存放日志数据
		Map<String, String> map = new HashMap<>();
		map.put("userId", (String) request.getSession().getAttribute(GlobalConstant.USE_ID));
		map.put("ip", ApiTools.getIpAddr(request));
		map.put("url", request.getRequestURI());
		map.put("time", DateUtils.getDateTime());
		map.put("agent", request.getHeader("USER-AGENT"));
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("参数:");
		Enumeration<String> paramers = request.getParameterNames();
		while (paramers.hasMoreElements()) {
			String key = paramers.nextElement();
			stringBuilder.append(key + "=" + request.getParameter(key));
		}
		map.put("paramers", stringBuilder.toString());
		log.info("saveToKafka : " + JSONObject.toJSONString(map));
		kafkaClientUtil.send(JSONObject.toJSONString(map));
	}
}
