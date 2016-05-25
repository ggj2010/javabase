package controlthread;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/6 16:48
 */
public class InterruptTest {

    /**
     * 单元测试interrupt没效果，只好用main方法测试咯
     * @throws Exception
     */
    @Test
    public void testMain() throws Exception {
       new Interrupt().main();
    }
}