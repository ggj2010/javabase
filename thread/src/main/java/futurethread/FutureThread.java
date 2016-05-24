package futurethread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/8 16:54
 */
@Slf4j
public class FutureThread {
    public static void main(String[] args) {
        new FutureThread().future();

    }

    public void future(){
        ExecutorService executorService= Executors.newCachedThreadPool();
        Future<String> result = executorService.submit(new NameTask());
          //shutdown不能少
        executorService.shutdown();
        //得到结果之前做点别的事情
        doOtherThing();
        try {
            log.info("返回结果："+result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();e.printStackTrace();
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
