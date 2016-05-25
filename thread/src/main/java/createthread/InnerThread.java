package createthread;

import lombok.extern.slf4j.Slf4j;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/6 14:24
 */
@Slf4j
public class InnerThread implements  MainStartInterface{
    public void mstart() {
        new Thread(){
            public void run(){
                log.info("匿名类实现Thread");
            }
        }.start();
    }
}
