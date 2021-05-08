package com.ggj.java.rpc.demo.spring.firstdemo.rpcserver.annation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GGJRPCService {

    Class<?> interfaceClass() default void.class;
    /**
     * 路径地址
     * @return
     */
    String url() default "";

    /**
     * 版本
     * @return
     */
    String version() default "";

    /**
     * 分组,暂时不实现
     * @return
     */
    String group() default "";
}
