package biosocket;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/11 16:14
 */
@Slf4j
public class ClinetSocket {

    public static void main(String[] args) {
        new ClinetSocket().normalClinet();
    }

    /**
     * 客户端先写 后读
     */
    public void normalClinet() {
        try {
            Socket socket = new Socket("localhost", 80);

            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            OutputStream outPutStream = socket.getOutputStream();
            PrintWriter ps = new PrintWriter(outPutStream);
            //客户端写过之后，再等待服务端的输入.结束的时候必须是客户端先输出
            for (int i = 0; i <3 ; i++) {
                if(i==2) {
                    ps.println("quit");
                }else{
                    ps.println("你好，服务端"+i);
                }
                ps.flush();
                    log.info("客户端接收到信息：" + br.readLine());
            }
            log.info("客户端：close");
            //如果客户端关闭了，服务端的输出流会一直得到null
             socket.close();
        } catch (IOException e) {
            log.error("客户端异常：" + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
