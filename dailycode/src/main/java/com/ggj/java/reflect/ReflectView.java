
package com.ggj.java.reflect;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author:gaoguangjin
 * @date:2018/4/9
 */
@Slf4j
public class ReflectView {
    private final static String serialVersionUID = "serialVersionUID";

    public static void main(String[] args) {
        classForNameAndLoad();
        // reflectField();
        // reflectMethod();
    }
    /**
     * 如果反射调用方法，则必须要实例化类，得到对象
     */
    private static void reflectMethod() {
        try {
            Class<?> clazz = Thread.currentThread().getContextClassLoader()
                    .loadClass(UserBean.class.getName());
            Object object = clazz.newInstance();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals("setName")) {
                    method.invoke(object, "方法赋值");
                }
            }
            log.info(object.toString());
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    /**
     * 如果反射给field赋值，必须要实例化类，得到对象
     */
    private static void reflectField() {
        try {
            Class<?> clazz = Class.forName("com.ggj.java.reflect.UserBean");
            Object object = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals("name")) {
                    field.setAccessible(true);
                    field.set(object, "field赋值");
                }
            }
            log.info(object.toString());
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    private static void classForNameAndLoad() {
        try {
            Class<UserBean> userBeanClass = UserBean.class;
            // ReflectView.class.getName()==com.ggj.java.reflect.UserBean
            Class<?> clazz = Class.forName("com.ggj.java.reflect.UserBean");
            log.info((userBeanClass == clazz) + "");
            Class<?> clazzTwo = Thread.currentThread().getContextClassLoader()
                    .loadClass(UserBean.class.getName());
        } catch (ClassNotFoundException e) {
            log.error("error", e);
        }

    }
}
