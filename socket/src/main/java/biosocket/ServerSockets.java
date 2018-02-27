package biosocket;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/11 16:14
 */
@Slf4j
public class ServerSockets {

    //    socket通信最好使用DataInputStream和DataOutputStream去封装读写的操作，或者用ObjectInputStream和ObjectOutputStream
    public static void main(String[] args) {
        new ServerSockets().normalSocket();
//        new ServerSockets().threadSocket();
    }

    /**
     * 服务端启动线程进行 读写
     */
    public void threadSocket() {
        try {
            ServerSocket socket = new ServerSocket(80);
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

    public Thread getThread(final Socket client) {
        return new Thread() {
            public void run() {
                executeThread(client);
            }
        };
    }

    private void executeThread(Socket client) {
        BufferedReader br = null;
        BufferedWriter bw = null;
        String message = null;
        try {
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            while ((message = br.readLine()) != null) {
                log.info("服务端接收到信息：" + message);
                if ("quit".equals(message)) {
                    break;
                }
                //如果使用write 而客户端是用readLine（）进行读取的就必须要对输出数据加上换行，不然客户端读取不到数据
                bw.write("你好，客户端\r\n");
                log.info("服务端flush()");
                bw.flush();
            }
        } catch (IOException e) {
            log.error("服务端异常：" + e.getLocalizedMessage());
        } finally {
            try {
                br.close();
                bw.close();
                client.close();
            } catch (Exception e) {
                log.error("关闭服务端异常：" + e.getLocalizedMessage());
            }
        }


    }

    /**
     * 服务端 先读取后 写
     */
    public void normalSocket() {
        Socket client = null;
        BufferedReader br = null;
        PrintWriter pw = null;
        try {
            //创建socket服务端
            ServerSocket server = new ServerSocket(8081);
            //等待client请求
            client = server.accept();
            //bio阻塞流，只有客户端连接了才会继续执行下面
            InputStreamReader isr = new InputStreamReader(client.getInputStream());
            br = new BufferedReader(isr);

            OutputStream outPutStream = client.getOutputStream();
            pw = new PrintWriter(outPutStream);
            while (true) {
                //输入流会堵塞，所以用循环会一直等待客户端的输入，客户端一旦有输出流，服务端就会有输入流，如果客户端的socket.close();那么br.readLine()就是null
                String message = br.readLine();
                log.info("服务端接收到信息：" + message);
                if ("quit".equals(message)) {
                    break;
                }
                pw.println("你好，客户端");
                pw.flush();
            }
        } catch (Exception e) {
            log.error("服务端异常：" + e.getLocalizedMessage());
        } finally {
            try {
                br.close();
                pw.close();
                client.close();
            } catch (Exception e) {
                log.error("关闭服务端异常：" + e.getLocalizedMessage());
            }
        }
    }


}
