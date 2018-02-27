package createthread;

import lombok.extern.slf4j.Slf4j;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/6 14:24
 */
@Slf4j
public class NormalThread extends  Thread implements  MainStartInterface{


    public void run() {
        log.info("Thread调用线程");
    }
    public void mstart(){
        new Thread();
        NormalThread nt=new NormalThread();
        nt.start();
    }

    public static void main(String[] args) {
        new NormalThread().mstart();
    }
}
