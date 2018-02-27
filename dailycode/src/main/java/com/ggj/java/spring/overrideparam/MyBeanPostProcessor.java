package com.ggj.java.spring.overrideparam;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * author:gaoguangjin
 * Description:
 * BeanPostProcessor接口作用是：如果我们需要在Spring容器完成Bean的实例化、
 * 配置和其他的初始化前后添加一些自己的逻辑处理，我们就可以定义一个或者多个BeanPostProcessor接口的实现，然后注册到容器中
 * Email:335424093@qq.com
 * Date 2016/2/19 10:15
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {


//   在任何初始化代码（比如配置文件中的init-method）调用之前调用
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof Person)
        System.out.println("BeanPostProcessor接口方法postProcessBeforeInitialization对属性进行更改！");
        return bean;
    }

//    在初始化代码调用之后调用
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof Person)
        System.out.println("BeanPostProcessor接口方法postProcessAfterInitialization对属性进行更改！");
        return bean;
    }


    public MyBeanPostProcessor() {
        super();
        System.out.println("这是BeanPostProcessor实现类构造器！！");
    }


}
