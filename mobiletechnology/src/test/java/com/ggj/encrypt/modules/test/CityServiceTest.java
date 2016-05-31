package com.ggj.encrypt.modules.test;

import com.ggj.encrypt.modules.BaseTest;
import com.ggj.encrypt.modules.test.bean.City;
import com.ggj.encrypt.modules.test.dao.xml.CityDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @author:gaoguangjin
 * @date 2016/5/31 11:11
 */
@Slf4j
public class CityServiceTest extends BaseTest {
    @Autowired
    private CityService cityService;

    @Test
    public void testGetCity() throws Exception {
        City city=cityService.getCity(1);
        log.info(city.toString());
    }

    @Test
    public void testInsert() throws Exception {
        City city=new City();
        city.setName("testInsert事务");
        city.setCountry("中国");
        city.setState("哈哈");
        cityService.insert(city);
    }

    @Test
    public void testInsertReadOnly() throws Exception {
        City city=new City();
        city.setName("testInsert事务readonly");
        city.setCountry("中国");
        city.setState("哈哈");
        cityService.insertReadOnly(city);
    }

    @Test
    public void testInsertAndGet() throws Exception {
        City city=new City();
        city.setName("testInsert事务insertAndGet");
        city.setCountry("中国");
        city.setState("哈哈");
        cityService.insertAndGet(city);
    }
}