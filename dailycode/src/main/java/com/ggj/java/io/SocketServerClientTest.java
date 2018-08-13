
package com.ggj.java.io;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author:gaoguangjin
 * @date:2018/4/11
 */
@Slf4j
public class SocketServerClientTest {

    AtomicInteger atomicInteger = new AtomicInteger();

    public static void main(String[] args) {
        //客户端和服务器简历一个连接，客户端连续发送两条消息，客户端关闭与服务端的连接
        try {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 1*1024*1024 ; i++) {
                sb.append("啊");
            }
            log.info(sb.length()+"");
            for (int i = 0; i < 600; i++) {
                new Thread() {
                    @Override
                    public void run() {
                        Socket socket = null;
                        try {
                            socket = new Socket("localhost", 8081);
                            InputStreamReader isr = new InputStreamReader(socket.getInputStream(),
                                    "utf-8");
                            OutputStream outPutStream = socket.getOutputStream();
                            PrintWriter ps = new PrintWriter(outPutStream);
                            ps.println(sb.toString());
                            ps.flush();
                            ps.println(sb.toString());
                            ps.flush();
                            socket.close();
                            System.out.println("发送完成");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
