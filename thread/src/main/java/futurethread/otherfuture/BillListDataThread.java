package futurethread.otherfuture;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author:gaoguangjin
 * @Description:
 * @Email:335424093@qq.com
 * @Date 2016/4/11 14:59
 */
@Slf4j
public class BillListDataThread {

    private FutureTask<List<Bill>> dataFuture;

    public BillListDataThread(){
        dataFuture=new  FutureTask<List<Bill>>(new BillDataThread());
        new Thread(dataFuture).start();
    }
    public List<Bill> getBillList() throws ExecutionException, InterruptedException {
        return dataFuture.get();
    }

    private class BillDataThread implements Callable<List<Bill>> {
        @Override
        public List<Bill> call() throws Exception {
            log.info("开始查询,模拟查询休眠10000秒");
            Thread.sleep(10000);
            log.info("查询结束");
            return new ArrayList<Bill>();
        }
    }
}
