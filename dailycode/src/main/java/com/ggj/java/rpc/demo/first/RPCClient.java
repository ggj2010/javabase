package com.ggj.java.rpc.demo.first;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

/**
 * @author gaoguangjin
 */
@Slf4j
public class RPCClient {
    public static void main(String[] args) {
        client();
    }

    public static void client() {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 8081);
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            printWriter.println(JSONObject.toJSON(new RpcBean("com.ggj.java.rpc.demo.first.TestServiceImpl", "testMethod", "ddd")));
            printWriter.flush();
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String readLine = null;
            while ((readLine = bufferedInputStream.readLine()) != null) {
                log.info("get client message {}", readLine);
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
