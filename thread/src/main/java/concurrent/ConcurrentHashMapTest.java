package concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author:gaoguangjin
 * Description:hashmap在多线程的环境下面会引起死循环
 * Email:335424093@qq.com
 * Date 2016/2/22 16:34
 */
public class ConcurrentHashMapTest {
    public static void main(String[] args) {

        //demo1();

//        主要是多线程同时put时，如果同时触发了rehash操作，会导致HashMap中的链表中出现循环节点，进而使得后面get的时候，会死循环
        //testLock();
    }

    private static void testLock() {
         final HashMap map = new HashMap();
        final Thread t1 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 500000; i++) {
                    map.put(new Integer(i), i);
                }
                System.out.println("t1 over");
            }
        };
        final Thread t2 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 500000; i++) {
                    map.put(new Integer(i), i);
                }
                System.out.println("t2 over");
            }
        };
        t1.start();
        t2.start();

    }

    private static void demo1() {
        Map<String, String> cmap = new ConcurrentHashMap<String, String>();
        HashMap<String, String> map = new HashMap<String, String>();
    }
}
