package controlthread;

import lombok.extern.slf4j.Slf4j;

/**
 * author:gaoguangjin
 * Description:当线程进去阻塞状态，调用thread.interrupt 会让线程提前结束阻塞，Object.wait, Thread.join和Thread.sleep 会让线程进入阻塞状态
 * Email:335424093@qq.com
 * Date 2016/1/6 16:03
 */
@Slf4j
public class Interrupt {

    public  void main() {
        Thread thread=thread();
        thread.start();
        thread.interrupt();
    }

    public static void main(String[] args) {
        new Interrupt().main();
    }

    /**
     * 让线程休眠N久，进入阻塞状态，调用interrupt会让线程提前结束，不休眠。
     * 如果线程不是阻塞状态，那么interrupt是没用的。
     */
    public  Thread  thread() {
       Thread thread=new Thread("线程1"){
           public void run(){
               log.info("线程执行开始");
               try {
                   Thread.sleep(1000000000);
               } catch (InterruptedException e) {
                   log.info("线程被打断，从阻塞状态跳出");
               }
               log.info("线程执行结束");
           }
        };
       return  thread;

    }

}
