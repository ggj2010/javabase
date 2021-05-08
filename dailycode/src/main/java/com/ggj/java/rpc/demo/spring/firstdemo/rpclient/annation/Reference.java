package com.ggj.java.rpc.demo.spring.firstdemo.rpclient.annation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD})
public @interface Reference {
    Class<?> interfaceClass() default void.class;
    String url() default "";
    String protocol() default "default";
    String serialize() default "hessian";
    String callType() default "sync";
    int timeout() default 1000;
    String callback() default "";
    String loadbalance() default "weightedAutoaware";
    int retries() default 1;
    boolean timeoutRetry() default false;
    String version() default "";
    String group() default "";
}
