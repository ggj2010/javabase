
package synchronizedthread;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author:gaoguangjin
 * @date:2018/4/18
 */
public class SynchronizedType {

    private final Map<String, String> myCache = new ConcurrentHashMap<String, String>(256);

    /**
     * double check and lock
     * @param name
     * @return
     */
    public String getValue(String name) {
        String value = myCache.get(name);
        if (value == null) {
            synchronized (this.myCache) {
                value = myCache.get(name);
                if (value == null) {
                    value = getValueFromDB();
                }
                myCache.put(value, value);
            }
        }
        return value;
    }

    public String getValueFromDB() {
        return new Random().nextInt(100) + "";
    }
}
