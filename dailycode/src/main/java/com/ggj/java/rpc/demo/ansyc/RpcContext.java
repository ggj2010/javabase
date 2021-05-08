package com.ggj.java.rpc.demo.ansyc;

import com.ggj.java.rpc.demo.ansyc.bean.RpcResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * @author gaoguangjin
 */
public class RpcContext {
    //存放requestId
    public static ThreadLocal<String> requestIdthreadLocal = new ThreadLocal<>();
    //调用方式
    public static Map<String, Boolean> invokeMethodStyle = new ConcurrentHashMap<>();
    //缓存future
    public static Map<String, RpcResultFuture> futureMap = new ConcurrentHashMap<>();

    /**
     * 返回值放到map里面
     * @param requestId
     * @param rpcResponse
     */
    public static void putAnsyResult(String requestId, RpcResponse rpcResponse) {
        RpcResultFuture future = futureMap.get(requestId);
        future.setResult(requestId, rpcResponse);
    }

    /**
     * 获取代理future
     *
     * @return
     */
    public static Future getFuture() throws Exception {
        if(requestIdthreadLocal.get()==null){
            throw new Exception("no ansyc method");
        }
        RpcResultFuture future = new RpcResultFuture();
        //存放到缓存，这样当返回值回来时候就可以用到
        futureMap.put(requestIdthreadLocal.get(), future);
        //防止随便get
        requestIdthreadLocal.remove();
        return future;
    }

    public static void setInvokeMethodStyle(String requestId) {
        invokeMethodStyle.put(requestId, true);
        requestIdthreadLocal.set(requestId);
    }

    public static boolean checkAnsyc(String requestId) {
        return invokeMethodStyle.get(requestId) == null ? false : true;
    }

}
