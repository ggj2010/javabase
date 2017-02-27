package com.ggj.encrypt.modules.sys.service;

import com.ggj.encrypt.common.annotation.catclient.CatTransaction;
import com.ggj.encrypt.modules.sys.bean.UserInfo;
import com.ggj.encrypt.modules.sys.dao.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 利用注解 自动去注册cat transation
 * @author:gaoguangjin
 * @date 2016/7/12 16:39
 */
@CatTransaction
@Service
public class CatAnnationService {
    @Autowired
    private UserInfoMapper userInfoMapperDao;

    public List<UserInfo> findAll() {
        return  userInfoMapperDao.findList(null);
    }
    public List<UserInfo> findAllTwo() {
        return  userInfoMapperDao.findList(null);
    }

}
