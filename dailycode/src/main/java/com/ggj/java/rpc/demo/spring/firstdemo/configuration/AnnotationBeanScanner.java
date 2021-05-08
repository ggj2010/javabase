package com.ggj.java.rpc.demo.spring.firstdemo.configuration;

import com.ggj.java.rpc.demo.spring.firstdemo.rpcserver.ServiceFactory;
import com.ggj.java.rpc.demo.spring.firstdemo.rpcserver.annation.GGJRPCService;
import com.ggj.java.rpc.demo.spring.firstdemo.rpcserver.config.ProviderConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * client和server端注解拦截
 *
 * @author gaoguangjin
 */
@Component
@Slf4j
public class AnnotationBeanScanner implements BeanPostProcessor, BeanFactoryPostProcessor, DisposableBean, ApplicationContextAware {

    //指定路径可以提高扫描速度，一般用于公司级别的路径
    private String[] annotationPackages = new String[]{"com.ggj.java.rpc.demo.spring.firstdemo"};


    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {


    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fieldsList = bean.getClass().getDeclaredFields();
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = AopUtils.getTargetClass(bean);
        if (!isMatchPackage(beanClass.getName())) {
            return bean;
        }
        GGJRPCService ggjrpcService = bean.getClass().getAnnotation(GGJRPCService.class);
        //防止是代理类
        if (ggjrpcService != null) {
            Class<?>[] interfaces = beanClass.getInterfaces();
            //默认的
            if (interfaces == null || interfaces.length == 0) {
                log.error("class:" + bean.getClass() + "with annation GGJRPCService  is not a interface,ignore");
                return bean;
            }
            ProviderConfig providerConfig = new ProviderConfig(interfaces[0], bean);
            providerConfig.setGroup(ggjrpcService.group());
            providerConfig.setService(bean);
            providerConfig.setUrl(ggjrpcService.url());
            providerConfig.setVersion(ggjrpcService.version());
            ServiceFactory.addService(providerConfig);
        }
        return ggjrpcService;
    }

    private boolean isMatchPackage(String beanName) {
        if (annotationPackages == null || annotationPackages.length == 0) {
            return true;
        }
        for (String annotationBasePackage : annotationPackages) {
            if (beanName.startsWith(annotationBasePackage)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    log.info("===");
    }
}
