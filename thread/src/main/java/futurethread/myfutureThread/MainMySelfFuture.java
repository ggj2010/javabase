package futurethread.myfutureThread;

import createthread.InnerThread;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author:gaoguangjin
 * Description:自己实现一个future模式
 * Email:335424093@qq.com
 * Date 2016/1/8 17:17
 */
@Slf4j
public class MainMySelfFuture {

    public static void main(String[] args) {
       new MainMySelfFuture().myself();
    }

    private void myself() {
        //获取用户信息
        Client client=new Client();
        Data data=client.getData();

        ExecutorService executorService= Executors.newCachedThreadPool();
        executorService.execute(new Thread(new InnerThread(data)));
        executorService.shutdown();

        doOtherThing();
        log.info("得到用户名称："+data.getRealName());
    }
    private void doOtherThing() {
        for (int i = 0; i <4 ; i++) {
            log.info("在等待得到用户名称过程中：处理其他的事情");
        }
    }

    class InnerThread implements Runnable{
       Data data;
       public InnerThread(Data data) {
           this.data=data;
       }

       public void run() {
           log.info("模拟调用远程数据库 查询用户名称......");
           try {
               Thread.sleep(3000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           log.info("休眠结束.......");
           data.setRealName("ggj");
       }
   }

}
