package com.ggj.java.rpc.demo.second;

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

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 8, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

    public static void init(final Object service) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8081);
        while (true) {
            Socket socket = serverSocket.accept();
            threadPoolExecutor.submit(new Thread() {
                @Override
                public void run() {
                    InputStream inputStream = null;
                    try {
                        inputStream = socket.getInputStream();
                        ObjectInputStream ois = new ObjectInputStream(inputStream);
                        String methodName = ois.readUTF();
                        Class<?>[] parameterTypes = (Class<?>[]) ois.readObject();
                        Object[] arg = (Object[]) ois.readObject();
                        Object result = service.getClass().getMethod(methodName, parameterTypes).invoke(service, arg);
                        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                        printWriter.println(result);
                        printWriter.flush();
                        printWriter.close();
                    } catch (Exception e) {
                        log.error("", e);
                    } finally {
                        try {
                            socket.close();
                            inputStream.close();
                        } catch (IOException e) {
                            log.error("", e);
                        }
                    }
                }
            });
        }
    }

    public static void main(String[] args) throws IOException {
        ProviderServer.init(new TestServiceImpl());
    }
}
