package createthread;

import lombok.extern.slf4j.Slf4j;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/6 14:23
 */
@Slf4j
public class ThreadRunnable implements Runnable ,MainStartInterface{

    public void run() {
        log.info("接口调用线程");
    }

    public void mstart() {
        new Thread(new ThreadRunnable()).start();
    }

    public static void main(String[] args) {
       new ThreadRunnable().mstart();
    }
}
