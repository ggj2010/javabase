package concurrentthred;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/8 15:45
 */
public class CountDownLatchUtilTest {

    @Test
    public void testUseCountDownLatch() throws Exception {
        new CountDownLatchUtil().useCountDownLatch();
    }
}