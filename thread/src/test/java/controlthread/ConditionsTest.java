package controlthread;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/7 15:43
 */
public class ConditionsTest {

    @Rule
    public ContiPerfRule rule = new ContiPerfRule();

    @PerfTest(invocations = 30)
    @Test
    public void testDisplayContion() throws Exception {
        new Conditions().displayContion();
    }
}