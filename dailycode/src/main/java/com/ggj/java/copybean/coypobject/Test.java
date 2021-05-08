package com.ggj.java.copybean.coypobject;

import com.ggj.java.copybean.StudentDTO;
import com.ggj.java.copybean.StudentEntity;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.BeanUtils;

/**
 * @author gaoguangjin
 */
public class Test {
    public static void main(String[] args) {
        TeacherDTO teacherDTO=new TeacherDTO();
        TeacherDTO teacherDTO2=new TeacherDTO();
        TeacherNew teacher=new TeacherNew();
        StudentDTO studentDTO=new StudentDTO();
        studentDTO.setName("gao");
        teacherDTO.setStudent(studentDTO);

        BeanUtils.copyProperties(teacherDTO,teacher);
        BeanUtils.copyProperties(teacherDTO,teacherDTO2);
        System.out.println("args = [" + args + "]");
         teacher=new TeacherNew();



        System.out.println("args = [" + args + "]");

    }
}
