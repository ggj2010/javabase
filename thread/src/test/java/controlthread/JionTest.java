package controlthread;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * author:gaoguangjin
 * Description:thread.join()应该是让当前线程block住，等thread执行完之后，再继续执行
 * Email:335424093@qq.com
 * Date 2016/1/6 16:33
 */
public class JionTest {

    // @Rule的注释下生成一个ContiPerfRule的对象 这个必须的加上的，不然没有作用
    @Rule
    public ContiPerfRule rule = new ContiPerfRule();

    Jion jion=new Jion();

    /**
     *  nvocations：方法的执行次数
     * @throws Exception
     */
    @Test
    @PerfTest(invocations = 5)
    public void testUserJion() throws Exception {
        jion.userJion();
    }

    @Test
   // @PerfTest(invocations = 5)
    public void testNotUserJion() throws Exception {
        jion.notUserJion();
    }
}