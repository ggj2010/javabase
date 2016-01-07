package productandconsume.userLock;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/7 16:31
 */
@Getter
@Setter
public class ProductLockBean {
    Lock lock;
    Condition condition;
    List<Integer> list;
    int maxProductListSize;
}
