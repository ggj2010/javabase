
package com.ggj.java.copybean;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import net.sf.cglib.beans.BeanCopier;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Java bean 拷贝 和效率对比
 */
@Slf4j
public class MyBeanUtils {

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        beanCopy();
        // testBeanCopyEfficiency();
    }

    private static void testBeanCopyEfficiency() throws InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        int size = 10000 * 1000;
        List<StudentEntity> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            StudentEntity studentEntity = new StudentEntity();
            studentEntity.setId(1);
            studentEntity.setName("高广金");
            studentEntity.setType("类型1");
            studentEntity.setAge(10);
            list.add(studentEntity);
        }

        long beginTime = System.currentTimeMillis();
        for (StudentEntity studentEntity : list) {
            StudentDTO springStudentDTO = new StudentDTO();
            BeanUtils.copyProperties(studentEntity, springStudentDTO);
        }
        long endTime = System.currentTimeMillis();

        log.info("spring bean copy 耗时：{}", endTime - beginTime);

        beginTime = System.currentTimeMillis();
        for (StudentEntity studentEntity : list) {
            StudentDTO cglibStudentDTO = new StudentDTO();
            BeanCopier beanCopier = BeanCopier.create(studentEntity.getClass(),
                    cglibStudentDTO.getClass(), false);
            beanCopier.copy(studentEntity, cglibStudentDTO, null);
        }
        endTime = System.currentTimeMillis();
        log.info("cglib copy 耗时：{}", endTime - beginTime);

        beginTime = System.currentTimeMillis();
        for (StudentEntity studentEntity : list) {
            StudentDTO studentDTO = (StudentDTO) MyBeanUtils.copy(studentEntity, StudentDTO.class);
        }
        endTime = System.currentTimeMillis();
        log.info("my bean copy 耗时：{}", endTime - beginTime);
    }

    private static void beanCopy() throws InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(1);
        studentEntity.setName("高广金");
        studentEntity.setType("类型1");
        studentEntity.setAge(10);
        studentEntity.setOrika("orika 没有重写getOrika2方法");
        log.info(studentEntity.toString());
        /**
         * spring beanutils自带的方式
         */
        StudentDTO springStudentDTO = new StudentDTO();
        BeanUtils.copyProperties(studentEntity, springStudentDTO);
        log.info("spring提供的方式：" + springStudentDTO.toString());
        /**
         * 自己实现的
         */
        StudentDTO myStudentDTO = (StudentDTO) MyBeanUtils.copy(studentEntity, StudentDTO.class);
        log.info("自己实现的：" + myStudentDTO.toString());

        StudentDTO cglibStudentDTO = new StudentDTO();
        BeanCopier beanCopier = BeanCopier.create(StudentEntity.class, StudentDTO.class, false);
        beanCopier.copy(studentEntity, cglibStudentDTO, null);
        log.info("cglib实现的：" + cglibStudentDTO.toString());

        /**
         * orika 可以实现list 和单个的拷贝
         * 可以实现指定不同字段名称相同类型的直接拷贝
         */
        List<StudentEntity> list= Arrays.asList(studentEntity);
        StudentDTO orikaStudentDTO = new StudentDTO();
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(StudentEntity.class, StudentDTO.class).field("orika", "orika2").byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        mapper.map(studentEntity, orikaStudentDTO);
        log.info("orika实现的：" + orikaStudentDTO.toString());
        //list形式
        List<StudentDTO> orikaList = mapper.mapAsList(list, StudentDTO.class);
        log.info("orika list实现的：" + orikaList.toString());
    }

    private static Object copy(Object object, Class<?> target) throws IllegalAccessException,
            InstantiationException, InvocationTargetException, NoSuchMethodException {
        // 实例化赋值
        Object targetObject = target.newInstance();
        Class<?> targetclass = targetObject.getClass();
        Class<?> clas = object.getClass();
        Method[] methods = clas.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                Object value = method.invoke(object);
                try {
                    // 防止某些方法没有
                    targetclass.getDeclaredField(method.getName().substring(3, 4).toLowerCase()
                            + method.getName().substring(4, method.getName().length()));
                } catch (Exception e) {
                    continue;
                }
                Method setMethod = targetclass.getDeclaredMethod(
                        method.getName().replace("get", "set"), method.getReturnType());
                setMethod.invoke(targetObject, value);
            }
        }
        return targetObject;
    }
}
