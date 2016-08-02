package com.ggj.encrypt.modules.sys.service;

import java.util.List;

import com.ggj.encrypt.common.annation.catclient.method.CatMethodTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ggj.encrypt.common.annation.catclient.CatTransaction;
import com.ggj.encrypt.modules.sys.bean.UserInfo;
import com.ggj.encrypt.modules.sys.dao.UserInfoMapper;

/**
 * 利用注解 自动去注册cat transation
 * @author:gaoguangjin
 * @date 2016/7/12 16:39
 */
@Service
public class CatAnnationMethodService {
    @Autowired
    private UserInfoMapper userInfoMapperDao;

    @CatMethodTransaction(type = "CatAnnationMethodService",name = "findAll()")
    public List<UserInfo> findAll() {
        return  userInfoMapperDao.findList(null);
    }

    @CatMethodTransaction(type = "CatAnnationMethodService",name = "findAllTwo()")
    public List<UserInfo> findAllTwo() {
        return  userInfoMapperDao.findList(null);
    }
    @CatMethodTransaction(type = "CatAnnationMethodService",name = "findAllThress()")
    public List<UserInfo> findAllThress(String name) {
        return  userInfoMapperDao.findList(null);
    }

}
