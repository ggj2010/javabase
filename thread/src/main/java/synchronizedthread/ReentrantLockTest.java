package synchronizedthread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一、当两个并发线程访问同一个对象object中的这个synchronized(this)同步代码块时，一个时间内只能有一个线程得到执行。另一个线程必须等待当前线程执行完这个代码块以后才能执行该代码块。
 二、然而，当一个线程访问object的一个synchronized(this)同步代码块时，另一个线程仍然可以访问该object中的非synchronized(this)同步代码块。
 三、尤其关键的是，当一个线程访问object的一个synchronized(this)同步代码块时，其他线程对object中所有其它synchronized(this)同步代码块的访问将被阻塞。
 四、第三个例子同样适用其它同步代码块。也就是说，当一个线程访问object的一个synchronized(this)同步代码块时，它就获得了这个object的对象锁。结果，其它线程对该object对象所有同步代码部分的访问都被暂时阻塞。
 五、以上规则对其它对象锁同样适用.
 * @author:gaoguangjin
 * @date 2016/9/20 13:21
 */
@Slf4j
public class ReentrantLockTest {
    //可重入锁
    Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        ReentrantLockTest reentrantLockTest=new ReentrantLockTest();
        new Thread(){
            @Override
            public void run() {
                reentrantLockTest.stepOne("1");
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                reentrantLockTest.stepOne("2");
            }
        }.start();

    }


    public  void stepOne(String name){
        try{
            log.info("stepOne：尝试获取锁 {}",name);
            lock.lock();
            log.info("stepOne：得到锁 {}",name);
            stepTwo(name);
        }catch (Exception e){
        }finally {
            log.info("stepOne：释放锁 {}",name);
            lock.unlock();
        }
    }
    public  void stepTwo(String name){
        try{
            lock.lock();
            log.info("stepTwo：得到锁 {}",name);
        }catch (Exception e){

        }finally {
            log.info("stepTwo：释放锁 {}",name);
            lock.unlock();
        }
    }
}
