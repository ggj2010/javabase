package chapter05;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 10-19
 */
public class ProcessData {
    private static final ReentrantReadWriteLock rwl       = new ReentrantReadWriteLock();
    private static final Lock                   readLock  = rwl.readLock();
    private static final Lock                   writeLock = rwl.writeLock();
    private volatile boolean                    update    = false;

    public void processData() {
        readLock.lock();
        if (!update) {
            // 必须先释放读锁
            readLock.unlock();
            // 锁降级从写锁获取到开始
            writeLock.lock();
            try {
                if (!update) {
                    // 准备数据的流程（略）
                    update = true;
                }
                readLock.lock();
            } finally {
                writeLock.unlock();
            }
            // 锁降级完成，写锁降级为读锁
        }
        try {
            // 使用数据的流程（略）
        } finally {
            readLock.unlock();
        }
    }

}
