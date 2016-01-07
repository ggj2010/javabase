package synchronizedthread;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/7 13:15
 */
public class LockThreadTest {
    /**
     * 全局变量lock测试
     * @throws Exception
     */
    @Test
    public void testUseGlobalLock() throws Exception {
        LockThread lockThread=new LockThread();
        lockThread.useGlobalLock();
    }

    /**
     * 局部变量lock测试，局部变量的lock 不会同步线程
     * @throws Exception
     */
    @Test
    public void testUseLocallLock() throws Exception {
        LockThread lockThread=new LockThread();
        lockThread.useLocallLock();
    }

    @Test
    public void testUserReetrantReadWriterLock() throws Exception {
        LockThread lockThread=new LockThread();
        lockThread.userReetrantReadWriterLock();
    }

    @Test
    public void testUserSynchronizedLock() throws Exception {
        LockThread lockThread=new LockThread();
        lockThread.userSynchronizedLock();
    }

    @Test
    public void testTryLcok() throws Exception {
        LockThread lockThread=new LockThread();
        lockThread.tryLcok(true);
    }
}