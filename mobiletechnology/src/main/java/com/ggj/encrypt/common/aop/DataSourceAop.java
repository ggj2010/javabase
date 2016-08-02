package com.ggj.encrypt.common.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.ggj.encrypt.common.utils.mybatis.DataSourceContextHolder;

import lombok.extern.slf4j.Slf4j;

/**
 * aop 拦截 进行切换数据源
 * 如果service层 增加了@Transactional ，导致数据源MyAbstractRoutingDataSource的determineCurrentLookupKey()方法会在执行DataSourceAop拦截之前就进行全局事务绑定
 * 从而导致获取 DataSourceContextHolder.getJdbcType(); 一直都是空值
 * @author:gaoguangjin
 * @date 2016/5/30 17:44
 */
@Aspect
@Component
@Slf4j
public class DataSourceAop {
	
	@Before("execution(* com.ggj.encrypt.modules.*.dao..*.find*(..)) or execution(* com.ggj.encrypt.modules.*.dao..*.get*(..)) or execution(* com.ggj.encrypt.modules.*.dao..*.select*(..))")
	public void setReadDataSourceType() {
		DataSourceContextHolder.read();
		log.info("dataSource切换到：Read");
	}
	
	/*@Around("@annotation(org.springframework.transaction.annotation.Transactional)")
	public void setWriteDataSourceType(ProceedingJoinPoint joinPoint) throws Throwable {
		Transactional datasource = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(Transactional.class);
		if(datasource.readOnly()){
			DataSourceContextHolder.read();
			log.info("dataSource切换到：Read");
		}else{
			DataSourceContextHolder.write();
			log.info("dataSource切换到：write");
		}
		joinPoint.proceed();
	}*/
}
