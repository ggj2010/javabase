
package chaibao;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author:gaoguangjin
 * @date:2018/7/10
 */
@Slf4j
public class ClientSocket {
    private final static int clientSize = 2;

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(clientSize);
        for (int i = 0; i < clientSize; i++) {
            pool.execute(clientThread(i));
        }
    pool.shutdown();
    }

    private static Runnable clientThread(int i) {
        return new Thread(() -> {
            Socket client = null;
            try {
                client = new Socket("localhost", 8081);
                InputStream inputStream = client.getInputStream();
                OutputStream outputStream = client.getOutputStream();
                outputStream.write("aaa".getBytes("utf-8"));
                outputStream.flush();
                outputStream.write("bbb".getBytes("utf-8"));
                outputStream.flush();
                byte[] bytes = new byte[1024];
                outputStream.flush();
                inputStream.read(bytes);
                log.info("线程{}，收到回复，{}",i, new String(bytes, "utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (client != null) {
                        client.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
