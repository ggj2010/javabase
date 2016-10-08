package com.ggj.webmagic.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author:gaoguangjin
 * @date 2016/8/22 18:45
 */
@Controller
@Slf4j
public class BaseController {
	private String exceptionJson = "";
	
	@ExceptionHandler({Exception.class})
	public String exception(Exception e, HttpServletRequest request) {
		log.error("Exception异常：" + e.getMessage());
		return "error";
	}
}
