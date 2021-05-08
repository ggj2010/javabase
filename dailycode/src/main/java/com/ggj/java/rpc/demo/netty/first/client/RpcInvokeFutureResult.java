package com.ggj.java.rpc.demo.netty.first.client;

import com.ggj.java.rpc.demo.netty.first.vo.RpcResponse;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author gaoguangjin
 */
public class RpcInvokeFutureResult implements Future {

    private Map<String, RpcInvokeFutureResult> rpcResultMap;
    private String requestId;

    private RpcResponse result;

    public RpcInvokeFutureResult(String requestId, Map<String, RpcInvokeFutureResult> rpcResultMap) {
        this.rpcResultMap=rpcResultMap;
        this.requestId = requestId;
    }

    private CountDownLatch countDownLatch = new CountDownLatch(1);

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
    public RpcResponse get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        //去除map 里面缓存的指
        rpcResultMap.remove(requestId);
        return result;
    }

    @Override
    public RpcResponse get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        countDownLatch.await(timeout, unit);
        //去除map 里面缓存的指
        rpcResultMap.remove(requestId);
        if(result==null){
            throw new TimeoutException("timeout");
        }
        return result;
    }

    public void put(RpcResponse result) {
        this.result = result;
        countDownLatch.countDown();
    }

}
