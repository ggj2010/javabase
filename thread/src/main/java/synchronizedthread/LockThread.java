package synchronizedthread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * author:gaoguangjin
 * Description:look 和synchronized功能一样，但是效率高更高
 * Email:335424093@qq.com
 * Date 2016/1/6 18:47
 */
@Slf4j
public class LockThread {
    //可重入锁
    Lock lock = new ReentrantLock();

    //   读写锁
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();


    public static void main(String[] args) {
        final LockThread t = new LockThread();
        /**  1、正确使用lock,不可以将lock弄成局部变量，不然起不到同步的作用**/
//        t.useGlobalLock();
        /*会出现两个线程同事得到锁*/
//        t.useLocallLock();

        /**2、读写锁ReentrantReadWriteLock和synchronized的比较**/
        /**ReentrantReadWriteLock有读写锁，可以让所有线程同时读取，而写的锁相当于* synchronized功能**/
      //打印结果应该是单个线程从开始读到读完毕和单个从开始写到写完毕  读写穿插
//          t.userSynchronizedLock();
        //打印机结果是多个线程可以同时进行读，但是写单个线程从开始写到结束
//        t.userReetrantReadWriterLock();

        //trylock()功能提供两种，一种是不带参数的，如果当前线程获取不到锁就直接退出，一种是lock.tryLock(1000, TimeUnit.DAYS）等待n秒后如果拿不到锁再退出
       // t.tryLcok(false);
        //等待
//        t.tryLcok(true);

        //可重入测试
        testReetry();
    }

    private static void testReetry() {
        LockThread lockThread=new LockThread();
        new Thread(()->{
            lockThread.lock.lock();
            lockThread.lock.lock();
            System.out.println("");
        }).start();

    }

    public void tryLcok(final boolean isWait) {
        for (int i = 0; i <10 ; i++) {
            new Thread(){
                public void run(){
                    tryLock(Thread.currentThread().getName(),isWait);
                }
            }.start();;
        }
    }

    private void tryLock(String name,boolean isWait)  {
        if(!isWait) {
            while(true) {
                if (lock.tryLock()) {
                    try {
                        log.info("名称:" + name + ":线程【得到】了锁");
                    } catch (Exception e) {
                        log.error("" + e.getLocalizedMessage());
                    } finally {
                        log.info("名称:" + name + ":线程关闭了锁");
                        //如果不释放锁就会出现其他线程执行这个方法就会出现死循环
                        lock.unlock();
                    }
                    break;
                } else {
                    log.info("名称:" + name + ":线程trylock得不到锁，直接退出");
                }
            }
        }else{
            try {
                //如果拿不到锁 就等待10秒
            if (lock.tryLock(10000, TimeUnit.DAYS)) {
                 log.info("名称:"+name + ":线程【得到】了锁");
            } else {
                log.info("名称:"+name + ":线程trylock得不到锁，直接退出");
            }
            } catch (Exception e) {
                log.error("" + e.getLocalizedMessage());
            } finally {
                log.info("名称:"+name + ":线程关闭了锁");
                lock.unlock();
            }
        }
    }

    public void userReetrantReadWriterLock() {
        startThreadbyType(4);
    }

    public void userSynchronizedLock() {
        startThreadbyType(3);
    }


    public void useGlobalLock() {
        startThreadbyType(1);
    }

    public void useLocallLock() {
        startThreadbyType(2);
    }

    /**
     * @param type 启动lock类型 1为全局变量，2为局部变量,3为synchroized,4为reetranreadwritelock
     */
    public void startThreadbyType(final int type) {
        for (int i = 0; i < 4; i++) {
            new Thread("线程" + i) {
                public void run() {
                    if (type == 1 || type == 2) {
                        dispaly(Thread.currentThread().getName(), type);
                    } else if (type == 3) {
                        synchroinzedRead(Thread.currentThread().getName());
                    } else if (type ==4) {
                        reetrantReadWriterLockRead(Thread.currentThread().getName());
                    }
                }
            }.start();
        }

        if (type == 3 || type == 4) {
            //写线程
            for (int i = 0; i < 10; i++) {
                new Thread("线程" + i) {
                    public void run() {
                        if(type==3) synchroinzedWrite(Thread.currentThread().getName());
                        if(type==4) reetrantReadWriterLockWrite(Thread.currentThread().getName());
                    }
                }.start();
            }
        }
    }

    /**
     * 多个线程一起进行读操作
     */
    private void reetrantReadWriterLockRead(String name) {
        readWriteLock.readLock().lock();
        try {
            for (int i = 0; i <2; i++) {
                log.info(name + "：==》开始读");
            }
            log.info(name + "：《==读取完毕");
        } catch (Exception e) {
            log.error("" + e.getLocalizedMessage());
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /**
     * 只允许一个线程来写。
     *
     * @param name
     */
    private void reetrantReadWriterLockWrite(String name) {
        readWriteLock.writeLock().lock();
        try {
            log.info(name + "：==》写数据");
            log.info(name + "：《==写完毕");
        } catch (Exception e) {
            log.error("" + e.getLocalizedMessage());
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private synchronized void synchroinzedRead(String name) {
        for (int i = 0; i < 2; i++) {
            log.info(name + "：==》开始读<(@￣︶￣@)> ");
        }
        log.info(name + "：《==读取完毕end");
    }

    private synchronized void synchroinzedWrite(String name) {
        log.info(name + "：==》写数据 ○(￣﹏￣)○ ");
        log.info(name + "：《==写完毕end");
    }

    /**
     * 打印局部变量锁和全部变量锁
     *
     * @param name
     * @param type
     */
    private void dispaly(String name, int type) {
        ReentrantLock localLock = null;
        //本地锁
        if (type == 2) {
            localLock = new ReentrantLock();
            localLock.lock();
        } else {
            //全局锁
            lock.lock();
        }
        try {
            log.info("得到锁线程:" + name);
        } catch (Exception e) {
            log.error("" + e.getLocalizedMessage());
        } finally {
            log.info("释放锁线程:" + name);
            //不同类型，不同释放锁
            if (type == 2) {
                localLock.unlock();
            } else {
                lock.unlock();
            }
        }
    }


}
