
package com.ggj.java.teach;

/**
 * @author:gaoguangjin
 * @date:2018/4/29
 */
public class Work {
    public Parent getWorkByName(String name) {
        if (name.contains("1")) {
            return new ChildOne();
        } else if (name.contains("2")) {
            return new ChildTwo();
        }
        return null;
    }
}
