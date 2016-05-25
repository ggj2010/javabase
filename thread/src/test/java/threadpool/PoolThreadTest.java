package threadpool;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/12 16:23
 */
public class PoolThreadTest {

    @Test
    public void testCachedThreadPool() throws Exception {
        PoolThread pt = new PoolThread();
        pt.cachedThreadPool();
    }

    @Test
    public void testFixedThread() throws Exception {
        PoolThread pt = new PoolThread();
        pt.fixedThread();
    }

    @Test
    public void testSingleThread() throws Exception {
        PoolThread pt = new PoolThread();
        pt.singleThread();
    }

    @Test
    public void testSchelThread() throws Exception {
        PoolThread pt = new PoolThread();
        pt.schelThread();
    }

    @Test
    public void testAwait() throws Exception {
        PoolThread pt = new PoolThread();
        pt.await();
    }
}