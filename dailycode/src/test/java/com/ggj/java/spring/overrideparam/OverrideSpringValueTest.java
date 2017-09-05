package com.ggj.java.spring.overrideparam;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.BaseTest;

import static org.junit.Assert.*;

public class OverrideSpringValueTest extends BaseTest{

    @Autowired
    private OverrideSpringValue overrideSpringValue;
    @Test
    public void getUrl() throws Exception {
        System.out.println(overrideSpringValue.getUrl());
    }

}