package com.ggj.java.rpc.demo.first;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author gaoguangjin
 */
@Slf4j
public class RPCServerDemo {
    public static void main(String[] args) {
        init();
    }

    public static void init() {
        Socket socket = null;
        try {
            ServerSocket serverSocket = new ServerSocket(8081);
            socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String readLine = null;
            while ((readLine = bufferedInputStream.readLine()) != null) {
                log.info("get client message {}", readLine);
                RpcBean rpcBean = JSONObject.parseObject(readLine, RpcBean.class);
                String calssName=rpcBean.getClassName();
                Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(calssName);
                Object obj = clazz.newInstance();
                String result= (String) clazz.getMethod(rpcBean.getMethod(), String.class).invoke(obj, rpcBean.getParam());
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                printWriter.println(result);
                printWriter.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
