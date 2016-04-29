package com.ggj.encrypt.common.utils;

import com.ggj.encrypt.common.utils.redis.RedisPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * @ClassName:SpringContextHolder.java
 * @Description:以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候取出ApplicaitonContext.
 * @author gaoguangjin
 * @Date 2015-11-19 下午5:16:39
 */
@Service
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

	private static ApplicationContext applicationContext = null;

	private static Logger logger = LoggerFactory.getLogger(SpringContextHolder.class);

	/**
	 * 取得存储在静态变量中的ApplicationContext.
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(Class<T> requiredType) {
		return applicationContext.getBean(requiredType);
	}

	/**
	 * 清除SpringContextHolder中的ApplicationContext为Null.
	 */
	public static void clearHolder() {
		if (logger.isDebugEnabled()) {
			logger.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
		}
		applicationContext.getBean(RedisPool.class).getJedisPool().close();
		logger.info("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
		applicationContext = null;
	}

	/**
	 * 实现DisposableBean接口, 在Context关闭时清理静态变量.
	 */
	@Override
	public void destroy() throws Exception {
		SpringContextHolder.clearHolder();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
		logger.info("                                           " +
				"◢████████████◣　　　　　　　 \n" +
				"　　　██████████████　　　　　　　 \n" +
				"　　　██　　　◥██◤　　　██　　　　　　　 \n" +
				"　◢███　　　　◥◤　　　　██◣　　　　　　 \n" +
				"　▊▎██◣　　　　　　　　◢█▊▊　　　　　　 \n" +
				"　▊▎██◤　　●　　●　　◥█▊▊　　　　　 \n" +
				"　▊　██　　　　　　　　　　█▊▊　　　　　　 \n" +
				"　◥▇██　▊　　　　　　▊　█▇◤　　　　　　 \n" +
				"　　　██　◥▆▄▄▄▄▆◤　█▊　　　◢▇▇◣ \n" +
				"◢██◥◥▆▅▄▂▂▂▂▄▅▆███◣　▊◢　█ \n" +
				"█╳　　　　　　　　　　　　　　　╳█　◥◤◢◤ \n" +
				"◥█◣　　　˙　　　　　˙　　　◢█◤　　◢◤　 \n" +
				"　　▊　　　　　　　　　　　　　▊　　　　█　　 \n" +
				"　　▊　　　　　　　　　　　　　▊　　　◢◤　　 \n" +
				"　　▊　　　　　　⊕　　　　　　█▇▇▇◤　　 \n" +
				"　◢█▇▆▆▆▅▅▅▅▆▆▆▇█◣　　　　　　 \n" +
				"　▊　▂　▊　　　　　　▊　▂　");
	}
	
}