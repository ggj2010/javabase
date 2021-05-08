package com.ggj.java.rpc.demo.netty.first.client;

import com.ggj.java.rpc.demo.netty.first.vo.RpcRequest;
import com.ggj.java.rpc.demo.netty.first.vo.RpcResponse;
import com.ggj.java.serializer.KryoUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 代理拦截
 *
 * @author gaoguangjin
 */
@Slf4j
public class RpcInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcInvokeFutureResult rpcInvokeFutureResult = null;
        RpcResponse response = null;
        try {
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setRequestId(UUID.randomUUID().toString());
            rpcRequest.setClassName(method.getDeclaringClass().getName());
            rpcRequest.setMethodName(method.getName());
            rpcRequest.setParameterTypes(method.getParameterTypes());
            rpcRequest.setParameters(args);
            //获取future返回值
            rpcInvokeFutureResult = ClientProvider.sendAndGetRpcInvokeFutureResult(rpcRequest, rpcRequest.getRequestId());
            response = rpcInvokeFutureResult.get(rpcRequest.getTimeOut(), TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("", e);
        }
        if (response != null) {
            return response.getResponse();
        }
        return null;
    }
}
