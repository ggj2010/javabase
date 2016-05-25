package futurethread.otherfuture;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author:gaoguangjin
 * @Description:模拟一个场景，比如我们要出一个报表，查询的数据返回的List,当然这个数据量很大，耗时很长，我们可以在查询的时候做点别的事情。
 * 因为要异步所以用线程，但是同时又要用到线程的结果，就用future模式，在获取线程结果这之间再搞点别的操作
 * @Email:335424093@qq.com
 * @Date 2016/4/11 15:00
 */
@Slf4j
public class MainBill {

    public static void main(String[] args) {
        BillListDataThread bt=new BillListDataThread();

        doMethod1();
        doMethod2();
        try {
            List<Bill> list=bt.getBillList();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void doMethod2() {
        try {
            log.info("执行其他操作耗时2000");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void doMethod1() {
        try {
            log.info("执行其他操作耗时2000");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
