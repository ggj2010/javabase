package productandconsume.userLock;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/7 16:21
 */

@Slf4j
public class Product extends Thread {

    Lock lock;
    Condition condition;
    List<Integer> list;
    int maxProductListSize;

    public Product(ProductLockBean plb) {
        this.lock = plb.getLock();
        this.condition = plb.getCondition();
        this.list = plb.getList();
        this.maxProductListSize = plb.getMaxProductListSize();
    }


    public void run() {
        try {
            while (true) {
                if (lock.tryLock()) {
                    if (list.size() == 0) {
                        for (int i = 0; i < maxProductListSize; i++) {
                            list.add(0);
                        }
                        log.info("生产者：宠物救助中心接收到" + maxProductListSize + "只猫！");
                        condition.signalAll();
                        lock.unlock();
                    } else {
                        condition.await();
                    }
                } else {
                    log.info("生产者：宠物救助中心 繁忙没有人员去抓猫（没有获取到锁）");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
