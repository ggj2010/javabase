
package com.ggj.java.reflect;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author:gaoguangjin
 * @date:2018/4/9
 */
@Setter
@Getter
@Slf4j
public class UserBean implements Serializable {
    static {
        log.info("执行static方法");
    }

    private static final long serialVersionUID = 4054288446570622586L;
    private String name;
    private String sex;

    public UserBean(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }

    public UserBean() {
    }

    private String test(String param) {
        return "method invoke:" + param;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
