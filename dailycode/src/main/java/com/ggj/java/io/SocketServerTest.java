
package com.ggj.java.io;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author:gaoguangjin
 * @date:2018/4/11
 */
@Slf4j
public class SocketServerTest {
    static AtomicInteger atomicInteger = new AtomicInteger();

    public static void main(String[] args) {
        try {
            // 创建socket服务端
            ServerSocket server = new ServerSocket(8081);
            while (true) {
                Socket clients = server.accept();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            InputStreamReader isr = new InputStreamReader(clients.getInputStream());
                            BufferedReader br = new BufferedReader(isr);
                            String str = br.readLine();
                            System.out.println(atomicInteger.incrementAndGet()+":"+str.length());
                        } catch (Exception e) {
                        }
                    }
                }.start();
                // 等待client请求
            }
        } catch (Exception e) {
            log.error("服务端异常：" + e.getLocalizedMessage());
        } finally {
        }
    }
}
