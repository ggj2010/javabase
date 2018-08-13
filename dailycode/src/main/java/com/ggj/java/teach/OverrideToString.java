
package com.ggj.java.teach;

/**
 * @author:gaoguangjin
 * @date:2018/4/29
 */

public class OverrideToString {
    private int age;
    private String name;

    public OverrideToString(String name, int i) {
    }

    public OverrideToString() {

    }
/*
    @Override
    public String toString() {
        return super.toString();
    }*/

    @Override
    public String toString() {
        return "重写 toString Method age=" + age + ";name=" + name;
    }

}
