package futurethread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author:gaoguangjin
 * @date 2016/5/20 14:07
 */
public class ListFutureTaskThread {
    private final static int length = 3;

    public static void main(String[] args) {
        futureTask();
    }

    private static void futureTask() {
        List<Callable<String>> list=new ArrayList<>();
        ExecutorService pool = Executors.newFixedThreadPool(length);
        for (int i = 0; i < length; i++)
            list.add(new NameTask());
        try {
            //当所有任务完成或超时期满时
            List<Future<String>> a = pool.invokeAll(list,20000, TimeUnit.MILLISECONDS);

            //得到结果之前做点别的事情
            doOtherThing();
            for (Future<String> stringFuture : a) {
                System.out.println(stringFuture.get());
            }
            pool.shutdown();
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }

    }

    private static void doOtherThing() {
        for (int i = 0; i <4 ; i++) {
            System.out.println("在等待得到用户名称过程中：处理其他的事情");
        }
    }
}
