package futurethread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * author:gaoguangjin
 * Description:future模式,可以获取线程处理完毕的结果
 * Email:335424093@qq.com
 * Date 2016/1/8 16:49
 */
@Slf4j
public class FutureTaskThread {

    public static void main(String[] args) {
        new FutureTaskThread().future();
    }

    public void future(){
        FutureTask<String> task=new FutureTask<String>(new NameTask());
        new Thread(task).start();
        try {
            //得到结果之前做点别的事情
            doOtherThing();

            //task.get需要在线程启动之后再执行！
            String result=task.get();
            log.info("返回结果："+result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    private void doOtherThing() {
        for (int i = 0; i <4 ; i++) {
            log.info("在等待得到用户名称过程中：处理其他的事情");
        }
    }

}
