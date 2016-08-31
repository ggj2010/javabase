package com.ggj.webmagic.tieba.service;

import com.ggj.webmagic.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 数据入库
 * @author:gaoguangjin
 * @date 2016/8/30 19:13
 */
public class TiBaImageServiceTest extends BaseTest{
    @Autowired
    private TiBaImageService tiBaImageService;
    @Test
    public void sychReisToMySql() throws Exception {
        tiBaImageService.sychReisToMySql();
    }
}