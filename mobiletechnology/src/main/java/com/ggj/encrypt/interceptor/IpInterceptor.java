package com.ggj.encrypt.interceptor;

import com.ggj.encrypt.common.constant.GlobalConstant;
import com.ggj.encrypt.common.exception.BizException;
import com.ggj.encrypt.common.utils.SpringContextHolder;
import com.ggj.encrypt.configuration.ResultCodeConfiguration;
import com.ggj.encrypt.security.LoginSecurity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/27 15:32
 */
@Slf4j
public class IpInterceptor implements HandlerInterceptor {
	private ResultCodeConfiguration resultCodeConfiguration = SpringContextHolder.getBean(ResultCodeConfiguration.class);
	private LoginSecurity loginSecurity = SpringContextHolder.getBean(LoginSecurity.class);
	
	/**
	 * 检验ip
	 * @param request
	 * @param response
	 * @param handler
	 * @throws Exception
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		response.setCharacterEncoding(GlobalConstant.DEFAULT_CHARSET);
		response.setHeader(GlobalConstant.DEFAULT_CONTENT_TYPE_NAME, GlobalConstant.DEFAULT_CONTENT_TYPE_VALUE);
		try {
			return loginSecurity.checkIPAndInvokeNumber(request, response);
		} catch (BizException e) {
			response.getOutputStream().write(e.getReturnRestult().toJSONString().getBytes(GlobalConstant.DEFAULT_CHARSET));
			response.getOutputStream().write(e.getReturnRestult().toJSONString().getBytes(GlobalConstant.DEFAULT_CHARSET));
		} catch (Exception e) {
			log.error("异常："+e.getLocalizedMessage());
			response.getOutputStream().write(resultCodeConfiguration.getErrorResultCode().getBytes(GlobalConstant.DEFAULT_CHARSET));
		}
		return false;
	}
	
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	}
}
