package com.ggj.java.rpc.demo.ansyc;

import com.ggj.java.rpc.demo.ansyc.bean.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用condition实现Future
 *
 * @author gaoguangjin
 */
@Slf4j
public class RpcResultVersionTwoFuture implements Future {
    private String requestId;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private Object object = null;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        try {
            lock.lock();
            //如果不加这行，会有问题，因为存在setResult回掉结果比get()早的情况。
            if (object == null) {
                condition.await();
            }
        } catch (InterruptedException e) {
            log.error("", e);
        } finally {
            lock.unlock();
        }
        //clear缓存
        RpcContext.futureMap.remove(requestId);
        return object;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        try {
            lock.lock();
            if (object == null) {
                condition.await(timeout, unit);
            }
        } catch (InterruptedException e) {
            log.error("", e);
        } finally {
            lock.unlock();
        }
        return object;
    }

    public void setResult(String requestId, RpcResponse rpcResponse) {
        this.requestId = requestId;
        object = rpcResponse;
        lock.lock();
        condition.signalAll();
        lock.unlock();
    }
}
