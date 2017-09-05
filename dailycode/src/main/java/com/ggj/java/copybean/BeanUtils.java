package com.ggj.java.copybean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanUtils {

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        beanCopy();
    }

    private static void beanCopy() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(1);
        studentEntity.setName("高广金");
        studentEntity.setType("类型1");
        studentEntity.setAge(10);

        StudentDTO studentDTO = (StudentDTO) copy(studentEntity, StudentDTO.class);
        System.out.println(studentEntity.toString());
        System.out.println(studentDTO.toString());
    }

    private static Object copy(Object object, Class<?> target) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        //实例化赋值
        Object targetObject = target.newInstance();
        Class<?> targetclass = targetObject.getClass();
        Class<?> clas = object.getClass();
        Method[] methods = clas.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                Object value = method.invoke(object);
                try {
                    //防止某些方法没有
                    targetclass.getDeclaredField(method.getName().substring(3,4).toLowerCase()+method.getName().substring(4,method.getName().length()));
                }catch (Exception e){
                    continue;
                }
                Method setMethod = targetclass.getDeclaredMethod(method.getName().replace("get", "set"), method.getReturnType());
                setMethod.invoke(targetObject, value);
            }
        }
        return targetObject;
    }
}
