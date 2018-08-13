package synchronizedthread;

/**
 * @author:gaoguangjin
 * @date:2018/4/4
 */
public class SynchronizedByteCode {
    private int age = 1;

    public synchronized void add() {
        age ++;
    }

    public void minus() {
        synchronized (this) {
            age--;
        }
    }

    public void test() {
        add();
        minus();
    }

    public static void main(String[] args) {
        new SynchronizedByteCode().test();
    }

}

