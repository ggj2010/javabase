package com.ggj.java.clientapi;


import java.util.concurrent.*;

public class ConfigFuture implements Future<ConfigValue> {

    private String key;
    private CountDownLatch latch;
    private Object result;

    public ConfigFuture(String key) {
        this.key = key;
        latch = new CountDownLatch(1);
    }

    @Override
    public boolean cancel(boolean arg0) {
        return false;
    }

    @Override
    public ConfigValue get() throws InterruptedException, ExecutionException {
        latch.await();
        if (result instanceof Throwable) {
            throw new ExecutionException((Throwable) result);
        }
        return (ConfigValue) result;
    }

    @Override
    public ConfigValue get(long arg0, TimeUnit arg1)
            throws InterruptedException, ExecutionException, TimeoutException {
        latch.await(arg0, arg1);
        if (result instanceof Throwable) {
            throw new ExecutionException((Throwable) result);
        }
        return (ConfigValue) result;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return result != null;
    }

    public void setResult(Object result) {
        this.result = result;
        latch.countDown();
    }

}
