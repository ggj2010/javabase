package threadpool;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

/**
 * Executor 接口
 * ExecutorService 接口  ExecutorService extends Executor
 * Executors  定义一系列 ThreadPoolExecutor工厂方法
 * ThreadPoolExecutor 实现ExecutorService
 * ThreadFactory 是用来创建线程的工厂类，可以定义线程的别名。
 * ThreadPoolExecutor 会定义两个线程变量一个是我们定义的线程，一个是通过工厂类创建的线程
 * 真正执行的是工厂类创建的线程，而我们定义的线程会直接调用run方法 而不是以start方式执行
 * this.firstTask = firstTask;
 * this.thread = getThreadFactory().newThread(this);
 * @author:gaoguangjin
 * @date 2017/1/9 9:20
 */
@Slf4j
public class ExecutorReview {

	public static void main(String[] args) throws InterruptedException {
		MyThreadPoolExecutor();
	}

	private static void MyThreadPoolExecutor() throws InterruptedException {
		ThreadPoolExecutor defaultThreadPool = new ThreadPoolExecutor(256, 256, 0L, TimeUnit.MILLISECONDS,
				new SynchronousQueue<Runnable>(), new NamedThreadFactory("async", true));
		defaultThreadPool.execute(getThread());
        //Daemon为true 主线程结束 ，多线程也会结束
		for(int i = 0; i < 10; i++) {
			System.out.println("结束");
			Thread.sleep(1000);
		}
	}

	public static Runnable getThread() {
		return new Thread() {

			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(3000);
						log.info("=====" + Thread.currentThread().getName());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
	}

	public static class NamedThreadFactory implements ThreadFactory {

		private static final AtomicInteger poolNumber = new AtomicInteger(1);

		private final ThreadGroup group;

		private final AtomicInteger threadNumber = new AtomicInteger(1);

		private final String namePrefix;

		private boolean mDaemo = false;

		public NamedThreadFactory(String name, boolean mDaemo) {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			namePrefix = name + "pool-" + poolNumber.getAndIncrement() + "-thread-";
			this.mDaemo = mDaemo;
		}

		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
			t.setDaemon(mDaemo);
			if (t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}
	}
}
