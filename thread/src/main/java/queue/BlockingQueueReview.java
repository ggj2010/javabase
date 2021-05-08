package queue;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * remove   移除并返回队列头部的元素     如果队列为空，则抛出一个NoSuchElementException异常
 * poll         移除并返问队列头部的元素     如果队列为空，则返回null
 * take        移除并返回队列头部的元素
 * add         增加一个元索                      如果队列已满，则抛出一个IIIegaISlabEepeplian异常
 * offer       添加一个元素并返回true        如果队列已满，则返回false
 * put         添加一个元素                       如果队列满，则阻塞
 * element  返回队列头部的元素              如果队列为空，则抛出一个NoSuchElementException异常
 * peek       返回队列头部的元素              如果队列为空，则返回null
 *
 * @author:gaoguangjin
 * 如果需要等待的就使用put()和take(),不需要等待的就是使用offer和poll
 * @Description:阻塞队列,有一个记录日志的业务场景，
 * 用户访问的日志不是立即追加到日志里面，而是先放到BlockingQueue里面，然后再开启一个定时任务去BlockingQueue里面拿去数据一次性处理
 * @Email:335424093@qq.com
 * @Date 2016/4/7 10:20
 */

@Slf4j
public class BlockingQueueReview {

    int number = 50;
    private BlockingQueue<Integer> arrayBlockingQueue = new ArrayBlockingQueue<Integer>(number);
    private BlockingQueue<Integer> testBlockingQueue = new ArrayBlockingQueue<Integer>(1);
    //大量插入和删除效率高,ReentrantLock put的时候进行加锁的
    private BlockingQueue<Integer> linkedBlockingDeque = new LinkedBlockingDeque<Integer>(number);

        /* 放入数据：
             　　offer(anObject):表示如果可能的话,将anObject加到BlockingQueue里,即如果BlockingQueue可以容纳,
             　　　　则返回true,否则返回false.（本方法不阻塞当前执行方法的线程）
             　　offer(E o, long timeout, TimeUnit unit),可以设定等待的时间，如果在指定的时间内，还不能往队列中
     　　　　加入BlockingQueue，则返回失败。
             　　put(anObject):把anObject加到BlockingQueue里,如果BlockQueue没有空间,则调用此方法的线程被阻断
     　　　　直到BlockingQueue里面有空间再继续.
             获取数据：
             　　poll(time):取走BlockingQueue里排在首位的对象,若不能立即取出,则可以等time参数规定的时间,
             　　　　取不到时返回null;
     　　poll(long timeout, TimeUnit unit)：从BlockingQueue取出一个队首的对象，如果在指定时间内，
             　　　　队列一旦有数据可取，则立即返回队列中的数据。否则知道时间超时还没有数据可取，返回失败。
             　　take():取走BlockingQueue里排在首位的对象,若BlockingQueue为空,阻断进入等待状态直到
     　　　　BlockingQueue有新的数据被加入;
     　　drainTo():一次性从BlockingQueue获取所有可用的数据对象（还可以指定获取数据的个数），
             　　　　通过该方法，可以提升获取数据效率；不需要多次分批加锁或释放锁。*/
    public static void main(String[] args) throws InterruptedException {
        BlockingQueueReview blockingQueueReview = new BlockingQueueReview();
        blockingQueueReview.linkedBlockingDeque();
//        blockingQueueReview.putLock();
        //执行这个注释其他的
//        blockingQueueReview.drainToLock();
    }

    private void drainToLock() throws InterruptedException {
        List<Integer> list=new ArrayList<>();
        testBlockingQueue.drainTo(list);
        log.info("drainTo 不堵塞");
        testBlockingQueue.take();
        log.info("take 堵塞");
        testBlockingQueue.drainTo(list);
    }

    private void putLock() throws InterruptedException {
        testBlockingQueue.put(1);
        log.info("不堵塞");
        testBlockingQueue.put(2);
        log.info("堵塞");
        testBlockingQueue.put(3);
    }

    /**
     * BlockingQueue是针对多线程来的，
     put() 时候如果BlockQueue没有空间,则调用此方法的线程被阻断
     　　　　直到BlockingQueue里面有空间再继续.
     take():取走BlockingQueue里排在首位的对象,若BlockingQueue为空,阻断进入等待状态直到
     　　　　BlockingQueue有新的数据被加入;
     *
     * @throws InterruptedException
     */
    public void linkedBlockingDeque() throws InterruptedException {
//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
//        executor.scheduleWithFixedDelay(getThread(), 0, 5000, TimeUnit.MILLISECONDS);
        new Thread(() -> {
            for (int i = 0; i < number*2; i++) {
                try {
//                    Thread.sleep(3000);
                    linkedBlockingDeque.put(i);

//                    linkedBlockingDeque.add(i);
//                    linkedBlockingDeque.offer(i);
                    log.info("put  linkedBlockingDeque大小=" + linkedBlockingDeque.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        while (true){
            Integer number = linkedBlockingDeque.take();
            log.info("线程拿出元素" + number);
            Thread.sleep(1000);
        }
    }

    private Runnable getThread() {
        return new Thread(() -> {
                     List<Integer> list=new LinkedList<Integer>();
                //一次性从BlockingQueue获取所有可用的数据对象
                try {
                    //poll 不会堵塞
                    //Integer number=linkedBlockingDeque.poll();
                    //每次只拿去一个 太麻烦了
                    Integer number = linkedBlockingDeque.take();
                    log.info("线程拿出元素" + number);
//                    Thread.sleep(3000);
                    //就获取number 大于linkedBlockingDeque目前的长度，那么就会直接拿去linkedBlockingDeque全部的长度
                    linkedBlockingDeque.drainTo(list,number);
//                    log.info("线程拿到数据size："+list.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        });
    }


}
