import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/27 17:20
 */
public class UseList {
    public static void main(String[] args) {
        demo1();
    }

    private static void demo1() {
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("3");
        list.add("2");

        list.forEach(number -> System.out.println(number));
        new Thread( () -> System.out.println("In Java8, Lambda expression rocks !!") ).start();

    }
}
