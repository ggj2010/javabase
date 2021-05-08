package com.ggj.java.teach;

import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gaoguangjin
 */
public class ReferenceTest {

    public static void main(String[] args) {
        List<Student> studentList=new ArrayList<>();
        Student student=new Student();
        Student student2=new Student();
        student.setName("gaogao");
        studentList.add(student);
        student2=student;
        student=null;
        System.out.println(student==studentList.get(0));
        System.out.println(student2==studentList.get(0));

        test(student2);
    }

    private static void test(Student student2) {
        System.out.println("student2 = [" + student2 + "]");
        Student student3 = student2;
        student3.setName("333");
        System.out.println("student2 = [" + student2 + "]");
        System.out.println("student3 = [" + student3 + "]");
    }


    //@Data
    static class Student{
        @Setter
        private String name;

        @Override
        public String toString() {
            return "Student{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
