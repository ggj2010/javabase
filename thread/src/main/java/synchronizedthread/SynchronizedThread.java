package synchronizedthread;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * synchronized 关键字，它包括两种用法：synchronized 方法和 synchronized 块。
 * <p>
 * synchronized是对类的当前实例进行加锁，防止其他线程同时访问该类的该实例的所有synchronized块，注意这里是“类的当前实例”， 类的两个不同实例就没有这种约束了。
 * 那么static synchronized恰好就是要控制类的所有实例的访问了，static synchronized是限制线程同时访问jvm中该类的所有实例同时访问对应的代码快
 * 对于实例同步方法，锁是当前实例对象。
 * 对于静态同步方法，锁是当前对象的Class对象。
 * 对于同步方法块，锁是Synchonized括号里配置的对象。
 *
 * @author:gaoguangjin
 * @date 2016/9/19 18:11
 */
@Slf4j
public class SynchronizedThread {
    private List<String> lockListOne = new ArrayList<>();
    private List<String> lockListTwo = new ArrayList<>();

    public static void main(String[] args) {
        SynchronizedThread st = new SynchronizedThread();
        SynchronizedThread st2 = new SynchronizedThread();
        lockMethodTest(st);
        //类的两个不同实例
//        lockMethodWithDifferentClassTest(st,st2);
        //减少锁的持有时间
//        lockObjectTest(st,null);
        //类的两个不同实例
//        lockObjectWithDifferentClassTest(st, st2);
        //static是对当前类加锁的
//        lockStaticMethodTest(st,st2);

        //降低锁的粒度
//        reduceLockScope(st);
    }

    /**
     * 2019-09-05 17:14:44.405 [main] INFO  synchronizedthread.SynchronizedThread 64 reduceLockScope- cost time,2536
     * 2019-09-05 17:15:23.280 [main] INFO  synchronizedthread.SynchronizedThread 74 reduceLockScope- cost time,1248
     * @param st
     */
    private static void reduceLockScope(SynchronizedThread st) {
        long beginTime=System.currentTimeMillis();
        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(10);
        threadPoolExecutor.execute(new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    st.addListOne("1");

                    // 注释打开对比
//                    st.reduceLockScopeOne("1");
                }
            }
        });

        threadPoolExecutor.execute(new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    st.addListOne("1");
                    // 注释打开对比
//                    st.reduceLockScopeOne("1");
                }
            }
        });

        try {
            //如果不调用shutdown直接调用awaitTermination 会造成死锁
            threadPoolExecutor.shutdown();
            threadPoolExecutor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("",e);
        }
        log.info("cost time,{}",(System.currentTimeMillis()-beginTime));
    }

    private synchronized void addListOne(String code) {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lockListOne.add(code);
    }

    private synchronized void addListTwo(String code) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lockListOne.add(code);
    }

    private  void reduceLockScopeOne(String code) {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (lockListOne) {
            lockListOne.add(code);
        }
    }

    private  void reduceLockScopeTwo(String code) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (lockListTwo) {
            lockListOne.add(code);
        }
    }


    /**
     * 2017-02-28 09:55:00.617 [Thread-2] INFO  synchronizedthread.SynchronizedThread 117 lockObject- out name:B
     * 2017-02-28 09:55:00.619 [Thread-1] INFO  synchronizedthread.SynchronizedThread 117 lockObject- out name:A
     * 2017-02-28 09:55:00.619 [Thread-2] INFO  synchronizedthread.SynchronizedThread 117 lockObject- out name:B
     * 2017-02-28 09:55:00.619 [Thread-1] INFO  synchronizedthread.SynchronizedThread 117 lockObject- out name:A
     *
     * @param st
     * @param st2
     */
    private static void lockObjectWithDifferentClassTest(SynchronizedThread st, SynchronizedThread st2) {
        new Thread() {
            public void run() {
                st.lockObjectTest(st, st2);
            }
        }.start();
    }

    private static void lockMethodWithDifferentClassTest(SynchronizedThread st, SynchronizedThread st2) {
        new Thread() {
            public void run() {
                st.lockMethod("A");
            }
        }.start();
        new Thread() {
            public void run() {
                st2.lockMethod("B");
            }
        }.start();
    }

    /**
     * synchronized 如果没有static修饰是针对当前对象加锁的，如果加了static是对当前类加锁的
     *
     * @param st
     * @param st2
     */
    private static void lockStaticMethodTest(SynchronizedThread st, SynchronizedThread st2) {
        new Thread() {
            public void run() {
                st.lockStaticMethod("A");
            }
        }.start();
        new Thread() {
            public void run() {
                st2.lockStaticMethod("B");
            }
        }.start();

    }

    /**
     * 2019-09-05 14:45:51.369 [Thread-0] INFO  synchronizedthread.SynchronizedThread 133 lockObject- name:A
     * 2019-09-05 14:45:51.369 [Thread-1] INFO  synchronizedthread.SynchronizedThread 133 lockObject- name:B
     * 2019-09-05 14:45:51.372 [Thread-0] INFO  synchronizedthread.SynchronizedThread 133 lockObject- name:A
     * 2019-09-05 14:45:51.372 [Thread-1] INFO  synchronizedthread.SynchronizedThread 133 lockObject- name:B
     * 2019-09-05 14:45:51.372 [Thread-0] INFO  synchronizedthread.SynchronizedThread 133 lockObject- name:A
     * 2019-09-05 14:45:51.372 [Thread-1] INFO  synchronizedthread.SynchronizedThread 133 lockObject- name:B
     * 2019-09-05 14:45:51.372 [Thread-0] INFO  synchronizedthread.SynchronizedThread 133 lockObject- name:A
     * 2019-09-05 14:45:51.372 [Thread-1] INFO  synchronizedthread.SynchronizedThread 133 lockObject- name:B
     * 2019-09-05 14:45:51.373 [Thread-0] INFO  synchronizedthread.SynchronizedThread 133 lockObject- name:A
     * 2019-09-05 14:45:51.373 [Thread-1] INFO  synchronizedthread.SynchronizedThread 133 lockObject- name:B
     * 2019-09-05 14:45:51.373 [Thread-0] INFO  synchronizedthread.SynchronizedThread 133 lockObject- name:A
     * 2019-09-05 14:45:51.373 [Thread-1] INFO  synchronizedthread.SynchronizedThread 133 lockObject- name:B
     * 2019-09-05 14:45:51.373 [Thread-0] INFO  synchronizedthread.SynchronizedThread 133 lockObject- name:A
     * 2019-09-05 14:45:51.373 [Thread-1] INFO  synchronizedthread.SynchronizedThread 133 lockObject- name:B
     * 2019-09-05 14:45:51.373 [Thread-0] INFO  synchronizedthread.SynchronizedThread 133 lockObject- name:A
     * 2019-09-05 14:45:51.373 [Thread-1] INFO  synchronizedthread.SynchronizedThread 133 lockObject- name:B
     * 2019-09-05 14:45:51.373 [Thread-0] INFO  synchronizedthread.SynchronizedThread 133 lockObject- name:A
     * 2019-09-05 14:45:51.373 [Thread-1] INFO  synchronizedthread.SynchronizedThread 133 lockObject- name:B
     * 2019-09-05 14:45:51.374 [Thread-0] INFO  synchronizedthread.SynchronizedThread 133 lockObject- name:A
     * 2019-09-05 14:45:51.374 [Thread-1] INFO  synchronizedthread.SynchronizedThread 133 lockObject- name:B
     * 2019-09-05 14:45:51.374 [Thread-0] INFO  synchronizedthread.SynchronizedThread 135 lockObject- synchronized void lockObject:A
     * 2019-09-05 14:45:51.374 [Thread-0] INFO  synchronizedthread.SynchronizedThread 136 lockObject- out name:A
     * 2019-09-05 14:45:51.374 [Thread-0] INFO  synchronizedthread.SynchronizedThread 136 lockObject- out name:A
     * 2019-09-05 14:45:51.374 [Thread-0] INFO  synchronizedthread.SynchronizedThread 136 lockObject- out name:A
     * 2019-09-05 14:45:51.374 [Thread-0] INFO  synchronizedthread.SynchronizedThread 136 lockObject- out name:A
     * 2019-09-05 14:45:51.374 [Thread-0] INFO  synchronizedthread.SynchronizedThread 136 lockObject- out name:A
     * 2019-09-05 14:45:51.374 [Thread-0] INFO  synchronizedthread.SynchronizedThread 136 lockObject- out name:A
     * 2019-09-05 14:45:51.374 [Thread-0] INFO  synchronizedthread.SynchronizedThread 136 lockObject- out name:A
     * 2019-09-05 14:45:51.375 [Thread-0] INFO  synchronizedthread.SynchronizedThread 136 lockObject- out name:A
     * 2019-09-05 14:45:51.375 [Thread-0] INFO  synchronizedthread.SynchronizedThread 136 lockObject- out name:A
     * 2019-09-05 14:45:51.375 [Thread-0] INFO  synchronizedthread.SynchronizedThread 136 lockObject- out name:A
     * 2019-09-05 14:45:51.375 [Thread-1] INFO  synchronizedthread.SynchronizedThread 135 lockObject- synchronized void lockObject:B
     * 2019-09-05 14:45:51.375 [Thread-1] INFO  synchronizedthread.SynchronizedThread 136 lockObject- out name:B
     * 2019-09-05 14:45:51.375 [Thread-1] INFO  synchronizedthread.SynchronizedThread 136 lockObject- out name:B
     * 2019-09-05 14:45:51.375 [Thread-1] INFO  synchronizedthread.SynchronizedThread 136 lockObject- out name:B
     * 2019-09-05 14:45:51.375 [Thread-1] INFO  synchronizedthread.SynchronizedThread 136 lockObject- out name:B
     * 2019-09-05 14:45:51.375 [Thread-1] INFO  synchronizedthread.SynchronizedThread 136 lockObject- out name:B
     * 2019-09-05 14:45:51.376 [Thread-1] INFO  synchronizedthread.SynchronizedThread 136 lockObject- out name:B
     * 2019-09-05 14:45:51.376 [Thread-1] INFO  synchronizedthread.SynchronizedThread 136 lockObject- out name:B
     * 2019-09-05 14:45:51.376 [Thread-1] INFO  synchronizedthread.SynchronizedThread 136 lockObject- out name:B
     * 2019-09-05 14:45:51.376 [Thread-1] INFO  synchronizedthread.SynchronizedThread 136 lockObject- out name:B
     * 2019-09-05 14:45:51.376 [Thread-1] INFO  synchronizedthread.SynchronizedThread 136 lockObject- out name:B
     *
     * @param st
     * @param st2
     */
    private static void lockObjectTest(SynchronizedThread st, SynchronizedThread st2) {
        new Thread() {
            public void run() {
                st.lockObject("A");
            }
        }.start();
        new Thread() {
            public void run() {
                if (null == st2) {
                    st.lockObject("B");
                } else {
                    st2.lockObject("B");
                }
            }
        }.start();
    }

    private static void lockMethodTest(SynchronizedThread st) {
        new Thread() {
            public void run() {
                st.lockMethod("A");
            }
        }.start();
        new Thread() {
            public void run() {
                st.lockMethod("B");
            }
        }.start();
    }

    private static synchronized void lockStaticMethod(String name) {
        for (int i = 0; i < 10; i++) log.info("name:" + name);
    }

    private synchronized void lockMethod(String name) {
        while (true) {
            log.info("synchronized void lockMethod:" + name);
        }
    }

    /**
     * synchronized代码块的好处是可以对指定的位置进行加锁
     *
     * @param name
     */
    public void lockObject(String name) {
        for (int i = 0; i < 10; i++) log.info("name:" + name);
        synchronized (this) {
            log.info("synchronized void lockObject:" + name);
            for (int i = 0; i < 10; i++) log.info("out name:" + name);
        }
    }
}
