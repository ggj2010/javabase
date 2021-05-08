package com.ggj.java.rpc.demo.ansyc;


import com.ggj.java.rpc.demo.ansyc.bean.RpcRequest;
import com.ggj.java.rpc.demo.ansyc.bean.RpcResponse;
import com.ggj.java.rpc.demo.first.TestService;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * @author gaoguangjin
 */
@Slf4j
public class ConsumerClient {
    public static <T> T getProxyClass(Class<T> interfaceClass, RpcRequest rpcRequest) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = null;
                ObjectOutputStream oos = null;
                BufferedReader bufferedInputStream = null;
                try {
                    socket = new Socket("localhost", 8081);
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    String requestId = UUID.randomUUID().toString();
                    rpcRequest.setRequestId(requestId);
                    rpcRequest.setMethodName(method.getName());
                    rpcRequest.setGetParameterTypes(method.getParameterTypes());
                    rpcRequest.setArgs(args);
                    oos.writeObject(rpcRequest);
                    oos.flush();
                    if (rpcRequest.isAnsyc()) {
                        //requestid 放到threadLocal
                        RpcContext.setInvokeMethodStyle(requestId);
                        Socket finalSocket = socket;
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    ObjectInputStream ois = new ObjectInputStream(finalSocket.getInputStream());
                                    RpcResponse rpcResponse = (RpcResponse) ois.readObject();
                                    if (RpcContext.checkAnsyc(rpcResponse.getRequestId())) {
                                        RpcContext.putAnsyResult(rpcResponse.getRequestId(), rpcResponse);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    } else {
                        bufferedInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
                        return bufferedInputStream.readLine();
                    }
                    return null;
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                }
                return null;
            }
        });
    }

    public static void main(String[] args) throws Exception, InterruptedException {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setAnsyc(true);
        TestService testService = ConsumerClient.getProxyClass(TestService.class, rpcRequest);
        long beginTime = System.currentTimeMillis();
        testService.testMethod("gaogao1");
        Future future1 = RpcContext.getFuture();

        testService.testMethod("gaogao2");
        Future future2 = RpcContext.getFuture();

        log.info("------------get result-------");
        RpcResponse response1 = (RpcResponse) future1.get();
        System.out.println(response1.getResult());

        RpcResponse response2 = (RpcResponse) future2.get();
        long endTime = System.currentTimeMillis();

        log.info("cost:{}", endTime - beginTime);
        System.out.println(response2.getResult());
    }
}
