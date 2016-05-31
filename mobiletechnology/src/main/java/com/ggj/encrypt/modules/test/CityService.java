package com.ggj.encrypt.modules.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ggj.encrypt.modules.test.bean.City;
import com.ggj.encrypt.modules.test.dao.xml.CityDao;

/**
 * @author:gaoguangjin
 * @date 2016/5/31 10:59
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class CityService {
	@Autowired
	private CityDao cityDao;
	
	public City getCity(int id) {
		return cityDao.getCity(id);
	}
	
	@Transactional(readOnly = false)
	public void insert(City city) {
		cityDao.insert(city);
	}
	
	@Transactional(readOnly = true)
	public void insertReadOnly(City city) {
		cityDao.insert(city);
	}
	
	@Transactional(readOnly = true)
	public void insertAndGet(City city) {
		log.info(cityDao.getCity(1)+"");
		cityDao.insert(city);
	}
	
}
