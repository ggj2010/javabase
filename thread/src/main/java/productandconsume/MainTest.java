package productandconsume;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:MainTest.java
 * @Description: 生产者和消费者测试
 * @author gaoguangjin
 * @Date 2015-3-23 下午6:42:06
 */
public class MainTest {
	public static void main(String[] args) {
		Object object = new Object();
		List<Integer> list = new ArrayList<Integer>();
		
		Consume cs = new Consume(object, list);
		Product pd = new Product(object, list);
		
		new Thread(pd, "生产者").start();

		for (int i = 0; i <20 ; i++) {
			new Thread(cs, "消费者"+i).start();
		}
//		new Thread(cs, "消费者1").start();
//		new Thread(cs, "消费者2").start();
	}
}
