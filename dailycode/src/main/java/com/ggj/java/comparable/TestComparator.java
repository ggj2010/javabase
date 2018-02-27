package com.ggj.java.comparable;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Slf4j
public class TestComparator {
    public static void main(String[] args) {
        Student student1=new Student(1,"student1");
        Student student2=new Student(2,"student2");
        Student student3=new Student(3,"student3");
        List<Student> list=new ArrayList<>();
        list.add(student2);
        list.add(student1);
        list.add(student3);
        Collections.sort(list, new MyComparator());
        for (Student student : list) {
            log.info(student.getName());
        }
    }
}
