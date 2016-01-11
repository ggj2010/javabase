package biosocket;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/11 16:14
 */
@Slf4j
public class ServerSockets {


    public static void main(String[] args) {
        new ServerSockets().normalSocket();
    }

    /**
     * 服务端 先读取后 写
     */
    public void normalSocket() {
        Socket client= null;
        BufferedReader br = null;
        PrintWriter pw= null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            //创建socket服务端
            ServerSocket server = new ServerSocket(80);
            //等待client请求
             client = server.accept();
            //bio阻塞流，只有客户端连接了才会继续执行下面
            InputStreamReader isr = new InputStreamReader(client.getInputStream());
             br = new BufferedReader(isr);

            OutputStream outPutStream = client.getOutputStream();
             pw = new PrintWriter(outPutStream);
            while(true) {
                //输入流会堵塞，所以用循环会一直等待客户端的输入，客户端一旦有输出流，服务端就会有输入流，如果客户端的socket.close();那么br.readLine()就是null
                String message = br.readLine();
                log.info("服务端接收到信息：" + message);
                if("quit".equals(message)){
                    break;
                }
                pw.println("你好，客户端");
                pw.flush();
            }
        } catch (Exception e) {
            log.error("服务端异常：" + e.getLocalizedMessage());
        }finally {
            try {
                br.close();
                pw.close();
                client.close();
            }catch(Exception e){
            }
        }
    }


}
