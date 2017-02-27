package com.ggj.encrypt.common.annotation.catclient;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 大众点评cat event注解
 * @author:gaoguangjin
 * @date 2016/7/12 14:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CatEvent {
}
