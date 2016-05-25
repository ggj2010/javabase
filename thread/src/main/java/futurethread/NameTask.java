package futurethread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/8 16:54
 */
@Slf4j
public class NameTask implements Callable<String>{

    public String call() throws Exception {
        log.info("开始获取返回值：处理ing............ 休眠10000");
        //模拟现实业务处理耗费的时间
        Thread.sleep(10000);
        return "没男2010";
    }
}
