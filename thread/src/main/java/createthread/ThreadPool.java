package createthread;

import ch.qos.logback.core.util.ExecutorServiceUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author:gaoguangjin
 * Description:通过线程池创建线程
 * Email:335424093@qq.com
 * Date 2016/1/6 15:05
 */
@Slf4j
public class ThreadPool implements  MainStartInterface{

    public void createThread(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new PoolThread());
        executorService.shutdown();
    }

    public void mstart() {
        createThread();
    }

    class PoolThread extends  Thread{
        public void run(){
            log.info("线程池执行线程");
        }
    }

}
