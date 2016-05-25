package productandconsume;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Product implements Runnable {
	Object object;
	
	int maxNumber = 20;
	List<Integer> list;
	
	public Product(Object object, List<Integer> list) {
		this.list = list;
		this.object = object;
	}
	
	public void run() {
		while (true) {
			synchronized (object) {
				if (list.size() > 20) {
					try {
						log.info("生成的总数大于20了，不能再生成了噢！开始wait()==总数为" + list.size());
						object.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					for (int i = 0; i < 5; i++)
						list.add(1);
					log.info("生产者又生产了5个 剩余个数" + list.size());
					object.notifyAll();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		}
	}
}
