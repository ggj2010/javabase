package concurrentthred;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/8 15:44
 */
public class CyclicBarrierUtilTest {

    @Test
    public void testUserCyclicBarrierThread() throws Exception {
        new CyclicBarrierUtil().userCyclicBarrierThread();
    }

    @Test
    public void testUseCyclicBarrier() throws Exception {
        new CyclicBarrierUtil().useCyclicBarrier();

    }
}