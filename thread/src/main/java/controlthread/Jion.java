package controlthread;

import lombok.extern.slf4j.Slf4j;

/**
 * author:gaoguangjin
 * Description:jion 会让线程的执行按照一定顺序，jion的位置很重要，jion（）方法下面的代码总是会等这个线程执行完之后再执行
 * thread.join()应该是让当前线程block住，等thread执行完之后，再继续执行
 * Email:335424093@qq.com
 * Date 2016/1/6 16:21
 */
@Slf4j
public class Jion {

    /**
     * 让线程按照顺序执行，比如线程1执行完之后再执行线程2,3
     *
     * join的本质就是
     *
     *  while (isAlive()) {
            wait(0);
        }
     */
    public  void userJion() throws InterruptedException {
        Thread thread1= thread("线程1");
        Thread thread2= thread("线程2");
        Thread thread3= thread("线程3");

        /*join代码要写的位置是在它start后面 */
         thread1.start();
         thread1.join();

        thread2.start();
        thread2.join();

        thread3.start();
    }

    /**
     * 多执行几次，线程执行的顺序不一定是1,2,3，打印的结果可能2,1,3
     */
    public  void notUserJion() {
        Thread thread1= thread("线程1");
        Thread thread2= thread("线程2");
        Thread thread3= thread("线程3");

        thread1.start();
        thread2.start();
        thread3.start();
    }


    private static Thread  thread(final String name) {
        Thread thread=new Thread(name){
            public void run(){
                log.info("线程名称==> ："+Thread.currentThread().getName());
            }
        };
        return thread;
    }

}
