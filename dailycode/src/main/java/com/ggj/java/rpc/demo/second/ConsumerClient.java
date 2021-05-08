package com.ggj.java.rpc.demo.second;


import com.ggj.java.rpc.demo.first.TestService;
import com.ggj.java.rpc.demo.first.TestServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * @author gaoguangjin
 */
@Slf4j
public class ConsumerClient {
    public static <T> T getProxyClass(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = null;
                ObjectOutputStream oos = null;
                BufferedReader bufferedInputStream = null;
                try {
                    socket = new Socket("localhost", 8081);
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeUTF(method.getName());
                    oos.writeObject(method.getParameterTypes());
                    oos.writeObject(args);
                    oos.flush();
                    bufferedInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
                    return bufferedInputStream.readLine();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if(oos!=null) {
                            oos.close();
                            bufferedInputStream.close();
                            socket.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        });
    }



}
