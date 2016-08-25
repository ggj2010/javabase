package com.ggj.webmagic.base;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author:gaoguangjin
 * @date 2016/8/22 18:45
 */
@Slf4j
@ControllerAdvice
public class BaseControllerAdvice {
	/**
	 * 初始化数据绑定
	 * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
	 * 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
			
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}
		});
		/*
		 * // Date 类型转换 binder.registerCustomEditor(Date.class, new PropertyEditorSupport() { public void
		 * setAsText(String text) { setValue(DateUtils.parseDate(text)); } });
		 */
	}
}
