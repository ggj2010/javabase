package controlthread;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * author:gaoguangjin
 * Description:wait 和notify
 * Email:335424093@qq.com
 * Date 2016/1/6 18:04
 */
public class WaitTest {


    @Rule
    public ContiPerfRule rule = new ContiPerfRule();
    Wait wait = new Wait();

    /**
     * 不释放线程锁,用main方法查询结果，单元测试对所有方法带sleep()的显示结果都不一样！！！！！
     */
    @Test
   @PerfTest(invocations = 2)
    public void testNotReleaseLock() throws Exception {
        wait.notReleaseLock(wait, false);
    }

    @Test
    public void testReleaseLock() throws Exception {
        wait.releaseLock(wait,true);
    }
}