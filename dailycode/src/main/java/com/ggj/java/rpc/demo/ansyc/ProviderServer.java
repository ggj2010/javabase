package com.ggj.java.rpc.demo.ansyc;

import com.ggj.java.rpc.demo.ansyc.bean.RpcRequest;
import com.ggj.java.rpc.demo.ansyc.bean.RpcResponse;
import com.ggj.java.rpc.demo.first.TestServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author gaoguangjin
 */
@Slf4j
public class ProviderServer {

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

    public static void init(final Object service) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8081);
        while (true) {
            Socket socket  = serverSocket.accept();
            threadPoolExecutor.submit(new Thread() {
                @Override
                public void run() {
                    InputStream inputStream = null;
                    try {
                        inputStream = socket.getInputStream();
                        ObjectInputStream ois = new ObjectInputStream(inputStream);
                        RpcRequest rpcRequest = (RpcRequest) ois.readObject();
                        Object result = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getGetParameterTypes()).invoke(service, rpcRequest.getArgs());
                        RpcResponse response=new RpcResponse();
                        response.setResult(result);
                        response.setRequestId(rpcRequest.getRequestId());
                        ObjectOutputStream objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
                        objectOutputStream.writeObject(response);
                        objectOutputStream.flush();
                        System.out.println("return");
                    } catch (Exception e) {
                        log.error("", e);
                    } finally {
                    }
                }
            });
        }
    }

    public static void main(String[] args) throws IOException {
        ProviderServer.init(new TestServiceImpl());
    }
}
