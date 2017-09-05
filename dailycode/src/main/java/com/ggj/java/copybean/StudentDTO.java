package com.ggj.java.copybean;

/**
 * 暴露对外的实体类
 */
public class StudentDTO {
    private int id;
    private String name;

    private int age;
    //这个types 比较特殊
    private String types;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "StudentDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", types='" + types + '\'' +
                '}';
    }
}
