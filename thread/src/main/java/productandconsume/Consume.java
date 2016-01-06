package productandconsume;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Consume implements Runnable {
    Object object;
    List<Integer> list;

    public Consume(Object object, List<Integer> list) {
        this.list = list;
        this.object = object;
    }

    public void run() {
        while (true) {
            synchronized (object) {
                if (list.size() > 0) {
                    list.remove(0);
                    log.info("消费者开始消费1个,剩余" + list.size());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        log.info("剩余0个,消费者等待生产者生成！");
                        object.notifyAll();
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
