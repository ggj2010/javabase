package forkjion;

import java.util.concurrent.ForkJoinPool;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/28 10:38
 */
public class ForkJoinPoolDemo {

    public static void main(String[] args) {
        test();
    }

    private static void test() {
        //1,1,2，3
        Fibonacci task = new Fibonacci(4);
        ForkJoinPool forkJoinPool=new ForkJoinPool(4);
        forkJoinPool.invoke(task);
        System.out.println(task.getRawResult());
    }
}
