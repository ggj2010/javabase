package com.ggj.java.spring.autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:gaoguangjin
 * @date:2018/4/18
 */
@RestController
public class ControllerTest {
    @RequestMapping("")
    public void test(){
        System.out.println("");
    }
}
