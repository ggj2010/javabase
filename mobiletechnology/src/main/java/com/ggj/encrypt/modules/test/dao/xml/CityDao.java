package com.ggj.encrypt.modules.test.dao.xml;

import com.ggj.encrypt.modules.test.bean.City;
/**
 * @author:gaoguangjin
 * @date 2016/5/30 16:33
 */
public interface CityDao {
    City getCity(int id);
    void insert(City city);
}
