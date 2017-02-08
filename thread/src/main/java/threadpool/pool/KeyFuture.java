package threadpool.pool;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Future;


/**
 * <T> <E> <K,V>
 * @author:gaoguangjin
 * @date 2017/1/10 15:30
 */
@Getter
@Setter
public class KeyFuture<V> {
    private String key;
    private Future<V> future;
}
