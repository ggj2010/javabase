package threadpool;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/12 16:53
 */
public class PoolThreadPerformanceTest {

    @Test
    public void testNotPool() throws Exception {
        PoolThreadPerformance p = new PoolThreadPerformance();
        p.notPool();
    }

    @Test
    public void testUserPool() throws Exception {
        PoolThreadPerformance p = new PoolThreadPerformance();
        p.userPool();
    }
}