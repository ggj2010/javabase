package controlthread;

/**
 *     对DCL的分析也告诉我们一条经验原则：对引用（包括对象引用和数组引用）的非同步访问，即使得到该引用的最新值，却并不能保证也能得到其成员变量（对数组而言就是每个数组元素）的最新值。
 * @author:gaoguangjin
 * @date 2016/9/20 10:30
 */
public class Visibility extends Thread{
	
//	private boolean stop;
	private volatile boolean stop;

	public static void main(String[] args) throws Exception {
		Visibility v = new Visibility();
		v.start();
		Thread.sleep(1000);
		v.stopIt();
		Thread.sleep(2000);
		System.out.println("finish main");
		System.out.println(v.getStop());
	}
	
	public void run() {
		int i = 0;
		while(!stop) {
			i++;
		}
		System.out.println("finish loop,i=" + i);
	}
	
	public void stopIt() {
		stop = true;
	}
	
	public boolean getStop() {
		return stop;
	}
}
