
package chaibao;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TCP粘包
 * @author:gaoguangjin
 * @date:2018/7/10
 */
@Slf4j
public class ServerSockert {
    /**
     * 服务端收到粘包数据
     * 2018-07-10 17:59:40.712 [pool-1-thread-1] INFO  chaibao.ServerSockert 55 executeThread-服务端接收到信息1：aaabbb
     2018-07-10 17:59:40.713 [pool-1-thread-2] INFO  chaibao.ServerSockert 55 executeThread- 服务端接收到信息1：aaa
     2018-07-10 17:59:40.713 [pool-1-thread-2] INFO  chaibao.ServerSockert 58 executeThread- 服务端接收到信息2：bbb
     * @param args
     */
    public static void main(String[] args) {
        ServerSockert.threadSocket();
    }

    /**
     * 服务端启动线程进行 读写
     */
    public static void threadSocket() {
        try {
            ServerSocket socket = new ServerSocket(8081);
            ExecutorService pool = Executors.newCachedThreadPool();
            //// 同步改成异步去处理。多个客户端连接同一个服务端
            while (true) {
                Socket client = socket.accept();
                pool.execute(getThread(client));
            }
        } catch (IOException e) {
            log.error("threadSocket()服务端异常：" + e.getLocalizedMessage());
        }

    }

    public static Thread getThread(final Socket client) {
        return new Thread() {
            public void run() {
                executeThread(client);
            }
        };
    }

    private static void executeThread(Socket client) {
        String message = null;
        try {
            InputStream inputStream = client.getInputStream();
            OutputStream outputStream = client.getOutputStream();
            //因为一次性接受1024数据，如果client发送的两次数据，服务端可能收到的是一个数据包
            byte[] bytes = new byte[1024];
            inputStream.read(bytes);
            log.info("服务端接收到信息1：" + new String(bytes, "utf-8"));
            byte[] byte2 = new byte[1024];
            inputStream.read(byte2);
            log.info("服务端接收到信息2：" + new String(byte2, "utf-8"));
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            log.error("服务端异常：" + e.getLocalizedMessage());
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                log.error("关闭服务端异常：" + e.getLocalizedMessage());
            }
        }

    }
}
