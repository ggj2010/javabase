package com.ggj.encrypt.modules;

import com.ggj.encrypt.MainApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/3/31 15:02
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainApplication.class)
//If you want a web application to start up and listen on its normal port, so you can test it with HTTP (e.g. using RestTemplate),
// annotate your test class (or one of its superclasses) with @WebIntegrationTest
@WebIntegrationTest({"server.port=80", "management.port=80"})
public class BaseTest {
}
