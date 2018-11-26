package com.ggj.webmagic;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//@RunWith(SpringRunner.class)
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
// @RunWith(SpringJUnit4ClassRunner.class)
// @SpringApplicationConfiguration(classes = MainApplication.class)
@ActiveProfiles("beta")
public class BaseTest {
}
