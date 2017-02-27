package com.ggj.db.splittable.service;

import com.ggj.db.splittable.com.ggj.base.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author:gaoguangjin
 * @date 2017/2/22 14:33
 */
public class SplitTableServiceTest extends BaseTest{
    @Autowired
    private SplitTableService splitTableService;
    @Test
    public void prepareTableAndData() throws Exception {
        splitTableService.prepareTableAndData();
    }
}