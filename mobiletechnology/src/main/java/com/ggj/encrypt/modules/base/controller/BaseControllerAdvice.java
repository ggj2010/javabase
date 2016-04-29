package com.ggj.encrypt.modules.base.controller;

import java.beans.PropertyEditorSupport;
import java.util.Date;

import com.ggj.encrypt.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;


/**
 * @ClassName:BaseControllerAdvice.java
 * @Description:  所有@controller的父类。作用类似于**Controller extends BaseController  
 * @author gaoguangjin
 * @Date 2015-9-24 上午10:45:17
 */
@Slf4j
@ControllerAdvice
public class BaseControllerAdvice   {

	/**
	 * 初始化数据绑定
	 * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
	 * 2. 将字段中Date类型转换为String类型
	 */
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}
			
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
		});
	}
	




}
