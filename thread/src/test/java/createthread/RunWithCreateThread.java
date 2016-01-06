package createthread;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * author:gaoguangjin
 * Description:同时执行多个测试
 * Email:335424093@qq.com
 * Date 2016/1/6 14:53
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({InnerThreadTest.class,NormalThreadTest.class,ThreadRunnableTest.class,ThreadPoolTest.class})
public class RunWithCreateThread {
}
