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
public class Consume extends Thread {
	Lock lock;
	Condition condition;
	List<Integer> list;
	String threadName;
	
	public Consume(ProductLockBean plb, String threadName) {
		this.lock = plb.getLock();
		this.condition = plb.getCondition();
		this.list = plb.getList();
		this.threadName = threadName;
	}
	
	@Override
	public void run() {
		while (true) {
			//log.info("消费者：" + threadName + "------");
			try {
				lock.lock();
				if (list.size() == 0) {
					log.info("宠物救助中心没有猫咪，" + threadName + "在等待------");
					condition.signalAll();
					condition.await();
				} else {
					list.remove(0);
					log.info("消费者：" + threadName + "领取一只猫！剩余" + list.size());
					// break;
				}
			} catch (Exception e) {
				log.info("消费者：" + e.getLocalizedMessage());
			} finally {
				log.info("消费者：" + threadName + "带猫咪回家【 关闭锁】");
				lock.unlock();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
