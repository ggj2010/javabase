package com.ggj.java.spring.overrideparam;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.BaseTest;

import static org.junit.Assert.*;

public class OverrideSpringValueTest extends BaseTest{

    @Autowired
    private OverrideSpringValue overrideSpringValue;
    @Autowired
    private PropertiestService propertiestService;
    @Autowired
    private Person person;
    @Test
    public void getUrl() throws Exception {
        System.out.println(overrideSpringValue.getUrl());
    }

    @Test
    public void test() throws Exception {
        System.out.println(propertiestService.test());
    }

}