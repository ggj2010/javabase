package com.ggj.java.reflect;

import lombok.Getter;
import lombok.Setter;

/**
 * @author gaoguangjin
 */
public class Test {
    @Setter
    @Getter
    private Parent p;

    public static void main(String[] args) {
        Parent parent=new Parent();
        Test test=new Test();
        test.setP(parent);
        parent=null;
        System.out.println(parent+":" + test.getP());
    }


}
