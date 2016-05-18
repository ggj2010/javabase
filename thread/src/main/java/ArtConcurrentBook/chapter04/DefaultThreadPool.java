package chapter04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 6-20
 */
public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {
    // 线程池最大限制数
    private static final int      MAX_WORKER_NUMBERS     = 10;
    // 线程池默认的数量
    private static final int      DEFAULT_WORKER_NUMBERS = 5;
    // 线程池最小的数量
    private static final int      MIN_WORKER_NUMBERS     = 1;
    // 这是一个工作列表，将会向里面插入工作
    private final LinkedList<Job> jobs                   = new LinkedList<Job>();
    // 工作者列表
    private final List<Worker>    workers                = Collections.synchronizedList(new ArrayList<Worker>());
    // 工作者线程的数量
    private int                   workerNum              = DEFAULT_WORKER_NUMBERS;
    // 线程编号生成
    private AtomicLong            threadNum              = new AtomicLong();

    public DefaultThreadPool() {
        initializeWokers(DEFAULT_WORKER_NUMBERS);
    }

    public DefaultThreadPool(int num) {
        workerNum = num > MAX_WORKER_NUMBERS ? MAX_WORKER_NUMBERS : num < MIN_WORKER_NUMBERS ? MIN_WORKER_NUMBERS : num;
        initializeWokers(workerNum);
    }

    public void execute(Job job) {
        if (job != null) {
            // 添加一个工作，然后进行通知
            synchronized (jobs) {
                jobs.addLast(job);
                jobs.notify();
            }
        }
    }

    public void shutdown() {
        for (Worker worker : workers) {
            worker.shutdown();
        }
    }

    public void addWorkers(int num) {
        synchronized (jobs) {
            // 限制新增的Worker数量不能超过最大值
            if (num + this.workerNum > MAX_WORKER_NUMBERS) {
                num = MAX_WORKER_NUMBERS - this.workerNum;
            }
            initializeWokers(num);
            this.workerNum += num;
        }
    }

    public void removeWorker(int num) {
        synchronized (jobs) {
            if (num >= this.workerNum) {
                throw new IllegalArgumentException("beyond workNum");
            }
            // 按照给定的数量停止Worker
            int count = 0;
            while (count < num) {
                workers.get(count).shutdown();
                count++;
            }
            this.workerNum -= count;
        }
    }

    public int getJobSize() {
        return jobs.size();
    }

    // 初始化线程工作者
    private void initializeWokers(int num) {
        for (int i = 0; i < num; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            Thread thread = new Thread(worker, "ThreadPool-Worker-" + threadNum.incrementAndGet());
            thread.start();
        }
    }

    // 工作者，负责消费任务
    class Worker implements Runnable {
        // 是否工作
        private volatile boolean running = true;

        public void run() {
            while (running) {
                Job job = null;
                synchronized (jobs) {
                    // 如果工作者列表是空的，那么就wait
                    while (jobs.isEmpty()) {
                        try {
                            jobs.wait();
                        } catch (InterruptedException ex) {
                            // 感知到外部对WorkerThread的中断操作，返回
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    // 取出一个Job
                    job = jobs.removeFirst();
                }
                if (job != null) {
                    try {
                        job.run();
                    } catch (Exception ex) {
                        // 忽略Job执行中的Exception
                    }
                }
            }
        }

        public void shutdown() {
            running = false;
        }
    }
}
