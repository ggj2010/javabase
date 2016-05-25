package createthread;

import org.junit.Test;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/6 14:47
 */
public class InnerThreadTest {

    @Test
    public void testStart() throws Exception {
            new InnerThread().mstart();
    }
}