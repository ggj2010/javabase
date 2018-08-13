package com.ggj.java.spring.autowired;

import com.ggj.java.spring.overrideparam.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author:gaoguangjin
 * @date:2018/4/18
 */
@Service
public class AutowiredTest {
    /**
     * 这种写法会报错
     */
    @Autowired
    private Person person;

    /**
     * 这种写法没问题，因为当spring容器里面有两个相同类型的Person，会再继续根据变量名称（person1）去匹配spring容器里面的beanName
     */
    @Autowired
    private Person person1;

    public void test(){

    }
}
