package com.ggj.encrypt.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ggj.encrypt.common.exception.BizException;
import com.ggj.encrypt.common.utils.SpringContextHolder;
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
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		long beginTime = startTimeThreadLocal.get();
		long endTime = System.currentTimeMillis();
		long execTime = endTime - beginTime;
		// 大于5s,打印一下警告日志
		if (execTime > 200 || log.isInfoEnabled()) {
			log.info(request.getRequestURI() + " 访问时间过长, 耗费:" + execTime + " ms");
		}
	}
}
