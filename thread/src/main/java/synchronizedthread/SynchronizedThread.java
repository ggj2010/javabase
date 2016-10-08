package synchronizedthread;

import lombok.extern.slf4j.Slf4j;

/**
 * synchronized 关键字，它包括两种用法：synchronized 方法和 synchronized 块。
 *
 * synchronized是对类的当前实例进行加锁，防止其他线程同时访问该类的该实例的所有synchronized块，注意这里是“类的当前实例”， 类的两个不同实例就没有这种约束了。
 * 那么static synchronized恰好就是要控制类的所有实例的访问了，static synchronized是限制线程同时访问jvm中该类的所有实例同时访问对应的代码快
 *
 * @author:gaoguangjin
 * @date 2016/9/19 18:11
 */
@Slf4j
public class SynchronizedThread {
    public static void main(String[] args) {
        SynchronizedThread st=new SynchronizedThread();
        SynchronizedThread st2=new SynchronizedThread();
        SynchronizedThread st3=new SynchronizedThread();


        new Thread(){
            public void run() {
//                st.lockMethod("A");

//                st.lockObject("A");

//                lockStaticMethod("A");

//                st3.lockMethod("A");

                st3.lockStaticMethod("A");

            }
        }.start();
        new Thread(){
            public void run() {
//                st.lockMethod("B");
//                st.lockObject("B");
//                lockStaticMethod("b");
                //synchronized 不在static 是针对当前对象加锁的 加static是针对类加锁的
//                st2.lockMethod("B");
                //对类加锁的
                st2.lockStaticMethod("B");
            }
        }.start();
//        st.lockObject();
//        lockStaticMethod();
//        st.lockMethod();
        //重入锁
    }
    private  /*synchronized*/ void lockMethod(String name) {
        while(true) {
            log.info("synchronized void lockMethod:"+name);
        }
    }
    private static synchronized void lockStaticMethod(String name) {
        for (int i=0;i<10;i++)  log.info("name:"+name);
    }
    private  void lockObject(String name) {
        for (int i=0;i<10;i++)  log.info("name:"+name);
        synchronized (this){
                log.info("synchronized void lockObject:"+name);
        }
        for (int i=0;i<10;i++)  log.info("out name:"+name);
    }
}
