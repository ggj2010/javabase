package productandconsume.userLock;

import com.sun.org.apache.bcel.internal.generic.RETURN;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/7 16:22
 */
public class MainLockTest {

    public static void main(String[] args) throws Exception {
        ProductLockBean  plb=getProductLockBean();
        Product product= new Product(plb);
        product.start();


        for (int i = 0; i <20 ; i++) {
            new Consume(plb,"宠物爱好者"+i).start();
        }

    }

    private static ProductLockBean getProductLockBean() {
        Lock lock=new ReentrantLock();
        Condition condition=lock.newCondition();
        List<Integer> list=new ArrayList<Integer>();
        //生产者收养猫咪最多10个
        int maxProductListSize=10;

        ProductLockBean plb=new ProductLockBean();
        plb.setCondition(condition);
        plb.setList(list);
        plb.setMaxProductListSize(maxProductListSize);
        plb.setLock(lock);
        return plb;
    }
}
