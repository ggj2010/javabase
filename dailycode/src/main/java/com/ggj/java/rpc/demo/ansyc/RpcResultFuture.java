package com.ggj.java.rpc.demo.ansyc;

import com.ggj.java.rpc.demo.ansyc.bean.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author gaoguangjin
 */
@Slf4j
public class RpcResultFuture implements Future {
    private String requestId;
    private CountDownLatch countDownLatch = new CountDownLatch(1);
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
        countDownLatch.await();
        //clear缓存
        RpcContext.futureMap.remove(requestId);
        return object;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        countDownLatch.await(timeout, unit);
        return object;
    }

    public void setResult(String requestId, RpcResponse rpcResponse) {
        this.requestId = requestId;
        object = rpcResponse;
        countDownLatch.countDown();
    }
}
