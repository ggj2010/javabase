package com.ggj.encrypt.common.annation.catclient.method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 大众点评 cat 方法级别注解
 * @author:gaoguangjin
 * @date 2016/7/12 14:52
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CatMethodCache {
    public String  type() default "Cache.redis";
    //("add", "get", "mGet", "remove");
    public String  name() default "get";
}
