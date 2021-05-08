package concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CopyOnWrite容器即写时复制的容器。通俗的理解是当我们往一个容器添加元素的时候，不直接往当前容器添加，而是先将当前容器进行Copy，
 * 复制出一个新的容器，然后新的容器里添加元素，添加完元素之后，再将原容器的引用指向新的容器。这样做的好处是我们可以对CopyOnWrite容器进行并发的读，
 * 而不需要加锁，因为当前容器不会添加任何元素。所以CopyOnWrite容器也是一种读写分离的思想，读和写不同的容器。
 * CopyOnWriteArrayList的核心思想是利用高并发往往是读多写少的特性，对读操作不加锁，对写操作，先复制一份新的集合，
 * 在新的集合上面修改，然后将新集合赋值给旧的引用，并通过volatile 保证其可见性，当然写操作的锁是必不可少的了。
 * @author:gaoguangjin
 * @date 2016/5/6 9:58
 */
@Slf4j
public class CopyOnWriteArrayListTest {
    static List<String> list = new ArrayList<String>();
    //读不加锁 写加锁
    static List<String> copyOnWriteArrayList = new CopyOnWriteArrayList<String>();
    public static void main(String[] args) {
        //java中，List在遍历的时候，如果被修改了会抛出java.util.ConcurrentModificationException错误
//        exceptionTest(list);
        //CopyOnWriteArrayList类最大的特点就是，在对其实例进行修改操作（add/remove等）会新建一个数据并修改，修改完毕之后，再将原来的引用指向新的数组。这样，修改过程没有修改原来的数组。也就没有了ConcurrentModificationException错误。
        exceptionTest(copyOnWriteArrayList);
    }

    private static void exceptionTest(List<String> list) {
        Thread t=new Thread(()->{
            while (true) {
                list.add("1");
                System.out.println("1");
            }
        });
        //etDaemon(true)设置为守护线程，主线程结束后 守护线程也会推出
        //默认daemon fasle 是用户线程，意思就是当主线程都打印完了 守护线程也会继续执行
        t.setDaemon(true);
        t.start();

        try {
            Thread.currentThread().sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("error");
        }
        for (String s : list) {
            System.out.println(s);
        }

    }
}
