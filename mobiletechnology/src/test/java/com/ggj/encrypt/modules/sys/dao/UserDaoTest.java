package com.ggj.encrypt.modules.sys.dao;

import com.ggj.encrypt.modules.BaseTest;
import com.ggj.encrypt.modules.sys.bean.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/28 12:39
 */
@Slf4j
public class UserDaoTest extends BaseTest {
    @Autowired
    private UserDao userDao;

    public final static String LOGINNAME="gaoguangjin";
    public final static String PASSWORD="qazqaz";

    @Test
    public void testGetUserInfo() throws Exception {
        UserInfo userInfo=new UserInfo();
        userInfo.setLoginName(LOGINNAME);
        userInfo=userDao.getUserInfo(userInfo);
        if(userInfo!=null)
        log.info(userInfo.toString());
    }

    @Test
    public void testAddUserInfo() throws Exception {
        UserInfo userInfo=new UserInfo();
        userInfo.setId(1l);
        userInfo.setLoginName(LOGINNAME);
        userInfo.setPassword(PASSWORD);
        userDao.addUserInfo(userInfo);
    }

    @After
    public void after() throws Exception {
        testGetUserInfo();
    }

}