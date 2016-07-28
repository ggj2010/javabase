package com.ggj.encrypt.common.plugin;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;

import lombok.extern.slf4j.Slf4j;

/**
 * @author:gaoguangjin
 * @date 2016/7/13 11:27
 */
@Intercepts({
		@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class}),
		@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
@Slf4j
public class MyBatisCatPlugin implements Interceptor {
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object result = null;
		Transaction transaction = null;
		MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];
		Object objects = (Object)invocation.getArgs()[1];
		BoundSql boundSql = mappedStatement.getBoundSql(objects);
		// 得到 类名-方法
		String[] strArr = mappedStatement.getId().split("\\.");
		String class_method = strArr[strArr.length - 2] + "." + strArr[strArr.length - 1];
		String sql = getSql(mappedStatement.getConfiguration(), boundSql);
		try {
			transaction = Cat.getProducer().newTransaction("SQL", class_method);
			result = invocation.proceed();
			Cat.getProducer().logEvent("SQL.Method", mappedStatement.getSqlCommandType().name(), Message.SUCCESS, "");
			// Cat.getProducer().logEvent("SQL.Database","" ,Message.SUCCESS,"");
			Cat.getProducer().logEvent("SQL.Statement", sql.substring(0, sql.indexOf(" ")), Message.SUCCESS, sql);
			transaction.setStatus(Message.SUCCESS);
		} catch (InvocationTargetException | IllegalAccessException e) {
			transaction.setStatus( ((InvocationTargetException)e).getTargetException().toString());
			log.error( ((InvocationTargetException)e).getTargetException().toString());
			Cat.getProducer().logError( ((InvocationTargetException)e).getTargetException().toString(), e);
			throw e;
		} finally {
			// transaction.addData(boundSql.getSql().trim().replaceAll("\\n",""));
			transaction.complete();
		}
		return result;
	}
	
	public String getSql(Configuration configuration, BoundSql boundSql) {
		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
		if (parameterMappings.size() > 0 && parameterObject != null) {
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
				sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
			} else {
				MetaObject metaObject = configuration.newMetaObject(parameterObject);
				for(ParameterMapping parameterMapping : parameterMappings) {
					String propertyName = parameterMapping.getProperty();
					if (metaObject.hasGetter(propertyName)) {
						Object obj = metaObject.getValue(propertyName);
						sql = sql.replaceFirst("\\?", getParameterValue(obj));
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						Object obj = boundSql.getAdditionalParameter(propertyName);
						sql = sql.replaceFirst("\\?", getParameterValue(obj));
					}
				}
			}
		}
		return sql;
	}
	
	private String getParameterValue(Object obj) {
		String value = null;
		if (obj instanceof String) {
			value = "'" + obj.toString() + "'";
		} else if (obj instanceof Date) {
			DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			value = "'" + formatter.format(new Date()) + "'";
		} else {
			if (obj != null) {
				value = obj.toString();
			} else {
				value = "";
			}
		}
		return value;
	}
	
	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}
	
	@Override
	public void setProperties(Properties properties) {
		
	}
}
