package com.ggj.java.copybean;

/**
 * 实体类
 */
public class StudentEntity {
    private int id;
    private String name;
    private int age;
    private String type;
    private String orika;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypes() {
        return type;
    }

    public void setTypes(String type) {
        this.type = type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "StudentEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", type='" + type + '\'' +
                ", orika='" + orika + '\'' +
                '}';
    }

    public String getOrika() {
        return orika;
    }

    public void setOrika(String orika) {
        this.orika = orika;
    }
}
