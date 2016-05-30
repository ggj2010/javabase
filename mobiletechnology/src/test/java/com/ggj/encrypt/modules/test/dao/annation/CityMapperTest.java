package com.ggj.encrypt.modules.test.dao.annation;

import com.ggj.encrypt.modules.BaseTest;
import com.ggj.encrypt.modules.sys.dao.UserDao;
import com.ggj.encrypt.modules.test.bean.City;
import com.ggj.encrypt.modules.test.dao.xml.CityDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @author:gaoguangjin
 * @date 2016/5/30 16:57
 */
@Slf4j
public class CityMapperTest extends BaseTest {
//    @Autowired
//    private CityDao cityDao;
    @Autowired
    private CityMapper cityDao;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Rule
    public ContiPerfRule rule = new ContiPerfRule();

    /**
     * 测试6次 看看是不是循环调用各个库
     * @throws Exception
     */
    @Test
    @PerfTest(invocations = 6)
    public void testFindByState() throws Exception {
        City city=cityDao.getCity(1);
        log.info(city.toString());
    }

    @Test
    public void testInsert() throws Exception {

    }
}