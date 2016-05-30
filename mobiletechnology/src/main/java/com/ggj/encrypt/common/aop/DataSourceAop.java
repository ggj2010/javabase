package com.ggj.encrypt.common.aop;

import com.ggj.encrypt.common.utils.mybatis.DataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * aop 拦截 进行切换数据源
 * @author:gaoguangjin
 * @date 2016/5/30 17:44
 */
@Aspect
@Component
@Slf4j
public class DataSourceAop {

    @Before("execution(* com.ggj.encrypt.modules.*.dao..*.find*(..)) or execution(* com.ggj.encrypt.modules.*.dao..*.get*(..))")
    public void setReadDataSourceType() {
        DataSourceContextHolder.read();
        log.info("dataSource切换到：Read");
    }

    @Before("execution(* com.ggj.encrypt.modules.*.dao..*.insert*(..)) or execution(* com.ggj.encrypt.modules.*.dao..*.update*(..))")
    public void setWriteDataSourceType() {
        DataSourceContextHolder.write();
        log.info("dataSource切换到：write");
    }
}
