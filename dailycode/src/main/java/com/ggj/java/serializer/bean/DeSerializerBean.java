
package com.ggj.java.serializer.bean;

import java.io.Serializable;

/**
 * @author:gaoguangjin
 * @date:2018/4/21
 */

public class DeSerializerBean implements Serializable {
//    private static final long serialVersionUID = -1361196013666487577L;
    private static final long serialVersionUID = -1L;
    String name;
    int age;

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

    @Override
    public String toString() {
        return "SerializerBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
