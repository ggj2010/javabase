
package com.ggj.java.thread.myfuturethread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author:gaoguangjin
 * @date:2018/9/2
 */
@Slf4j
public class FutureThread<V> implements Runnable, Future {
    private Callable<V> callable;

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private V object = null;
    private volatile boolean done;
    private volatile boolean isCancelled;

    public FutureThread(Callable callable) {
        this.callable = callable;
    }

    @Override
    public void run() {
        try {
            lock.lockInterruptibly();
            V result = callable.call();
            object = result;
            done = true;
            condition.signalAll();
        } catch (InterruptedException e) {
            isCancelled = true;
        } catch (Exception e) {
            log.error("", e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        if (done) {
            return object;
        } else {
            lock.lock();
            if (done) {
                return object;
            }
            condition.await();
            lock.unlock();
            return object;
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
