package com.ggj.java;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

/**
 * author:gaoguangjin
 * Description:redis调用，redis一般使用连接池方式进行调用，这里只是为了记录
 * Email:335424093@qq.com
 * Date 2016/3/1 9:56
 */
@Slf4j
public class RedisAllKeyTest {

    static Jedis jedis;
   static  {
        jedis = new Jedis("123.56.118.135", 6379);
       jedis.auth("gaoguangjin");
    }
    public static void main(String[] args) {
        RedisAllKeyTest redisAllKeyTest=new RedisAllKeyTest();
       // redisAllKeyTest. persistence();

        log.info(jedis.sismember("dd","cc")+"");
        log.info(jedis.get("dd"));
    }

    // quit：关闭连接（connection）
    // auth：简单密码认证
    // help cmd： 查看cmd帮助，例如：help quit
    //连接操作命令
    public void connectCommand(){
        jedis.quit();
        jedis.auth("gaoguangjin");
    }

    // 2）持久化
    // save：将数据同步保存到磁盘
    // bgsave：将数据异步保存到磁盘
    // lastsave：返回上次成功将数据保存到磁盘的Unix时戳
    // shundown：将数据同步保存到磁盘，然后关闭服务

    public void persistence(){
//        jedis.save();
//        jedis.bgsave();
//        jedis.shutdown();
        Long time = jedis.lastsave();
        log.info("time="+time);
    }

    // 4）对value操作的命令
    // exists(key)：确认一个key是否存在
    // del(key)：删除一个key
    // type(key)：返回值的类型
    // keys(pattern)：返回满足给定pattern的所有key
    // randomkey：随机返回key空间的一个
    // keyrename(oldname, newname)：重命名key
    // dbsize：返回当前数据库中key的数目
    // expire：设定一个key的活动时间（s）
    // ttl：获得一个key的活动时间
    // select(index)：按索引查询
    // move(key, dbindex)：移动当前数据库中的key到dbindex数据库
    // flushdb：删除当前选择数据库中的所有key
    // flushall：删除所有数据库中的所有key
    //
    public void dealValue(){
        String key="gao";
        String newKey="gaogao";
        String expirewKey="gaogao";
        boolean isExists=jedis.exists(key);
        //1为删除成功 0为删除失败证明没有这个key 了
        Long number = jedis.del(key);
        //返回值的类型
        String type = jedis.type(key);

        //返回满足给定pattern的所有key,可以用来查询key
        Set<String> keysSet = jedis.keys("*");
        //随机返回key空间的一个
        String randomKey=jedis.randomKey();
        //重命名key
        jedis.rename(key,newKey);

        //返回当前数据库中key的数目
        long keySize=jedis.dbSize();


       //设定一个key的活动时间（s）
        jedis.set(expirewKey,"expireexpire");
        jedis.expire(expirewKey,100);
        //获得一个key的剩余活动时间
        long expireTime=jedis.ttl(expirewKey);

        //切换不同的库,切换当前操作命令对应的库 默认是0库
        jedis.select(1);
        //移动当前数据库中的key到dbindex数据库
        jedis.move(key,1);

        //清空当前库 与清空所有库
        jedis.flushDB();
        jedis.flushAll();
    }

    // 5）String
    // set(key, value)：给数据库中名称为key的string赋予值value
    // get(key)：返回数据库中名称为key的string的value
    // getset(key, value)：给名称为key的string赋予上一次的value
    // mget(key1, key2,…, key N)：返回库中多个string的value
    // setnx(key, value)：添加string，名称为key，值为value
    // setex(key, time, value)：向库中添加string，设定过期时间time
    // mset(key N, value N)：批量设置多个string的值
    // msetnx(key N, value N)：如果所有名称为key i的string都不存在
    // incr(key)：名称为key的string增1操作
    // incrby(key, integer)：名称为key的string增加integer
    // decr(key)：名称为key的string减1操作
    // decrby(key, integer)：名称为key的string减少integer
    // append(key, value)：名称为key的string的值附加value
    // substr(key, start, end)：返回名称为key的string的value的子串
    public void stringKey(){
        String key="stringKey";
        String key2="stringKey2";
        jedis.mset(key,"value1",key2,"value3");

        //返回的是修改前的值,先get  然后再set
        String oldValue=jedis.getSet(key,"value3");

        List<String> String = jedis.mget(key, key2);
        //Redis实现分布式锁，SETNX命令（SET if Not eXists）,如果不存在，才赋值。实现分布式锁的时候 最好加个 expire
         jedis.setnx(key,"SET if Not eXists");

        //设置key的值 同事设置expire时间，作用等于jedis.set(key,value) jedis.expire(key);
        jedis.setex(key,10,"gaogao");
        //value 自动递增
        jedis.incr("incrkey");
        //按照指定的递增因子递增
        jedis.incrBy("incrkey",2);
        //递减
        jedis.decr("dekey");
        jedis.decrBy("dekey",2);

        //追加
        jedis.append(key,"在原来基础上追加");
        //切割字符串
        jedis.substr(key,0,-1);
    }

    // 6）List
    // rpush(key, value)：在名称为key的list尾添加一个值为value的元素
    // lpush(key, value)：在名称为key的list头添加一个值为value的 元素
    // llen(key)：返回名称为key的list的长度
    // lrange(key, start, end)：返回名称为key的list中start至end之间的元素
    // ltrim(key, start, end)：截取名称为key的list
    // lindex(key, index)：返回名称为key的list中index位置的元素
    // lset(key, index, value)：给名称为key的list中index位置的元素赋值
    //
    // lrem(key, count, value)：删除count个key的list中值为value的元素
    // lpop(key)：返回并删除名称为key的list中的首元素
    // rpop(key)：返回并删除名称为key的list中的尾元素
    // blpop(key1, key2,… key N, timeout)：lpop命令的block版本。
    // brpop(key1, key2,… key N, timeout)：rpop的block版本。
    // rpoplpush(srckey, dstkey)：返回并删除名称为srckey的list的尾元素，
    // 　　　　　　　　　　　　　　并将该元素添加到名称为dstkey的list的头部

    public void list(){
        String key="list";
        //在list 头添加元素
        jedis.lpush(key,"1","2","3","4");
        //在list 尾部添加元素
        jedis.rpush(key,"0");
        //返回list长度
        jedis.llen(key);
        //返回list  从头部到尾部
        jedis.lrange(key,0,-1);
        //将list 截取下
        jedis.ltrim(key,0,2);
        //返回指定index的值
        jedis.lindex(key,0);

        //给指定的index的重新赋值  这个可以常用
        jedis.lset(key,0,"4");

        //删除count个key的list中值为value的元素,删除list里面值2个值为0
        jedis.lrem(key,2,"0");

        jedis.llen(key);

        //返回并删除名称为key的list中的首元素
        String topValue=jedis.lpop(key);
        //返回并删除名称为key的list中的尾元素
        String endCalue=jedis.rpop(key);

    }

    // 7）Set 无序，不重复
    // sadd(key, member)：向名称为key的set中添加元素member
    // srem(key, member) ：删除名称为key的set中的元素member
    // spop(key) ：随机返回并删除名称为key的set中一个元素
    // smove(srckey, dstkey, member) ：移到集合元素
    // scard(key) ：返回名称为key的set的基数
    // sismember(key, member) ：member是否是名称为key的set的元素
    // sinter(key1, key2,…key N) ：求交集
    // sinterstore(dstkey, (keys)) ：求交集并将交集保存到dstkey的集合
    // sunion(key1, (keys)) ：求并集
    // sunionstore(dstkey, (keys)) ：求并集并将并集保存到dstkey的集合
    // sdiff(key1, (keys)) ：求差集
    // sdiffstore(dstkey, (keys)) ：求差集并将差集保存到dstkey的集合
    // smembers(key) ：返回名称为key的set的所有元素
    // srandmember(key) ：随机返回名称为key的set的一个元素
    public void set(){
        String key="setKey";
        String key2="setKey2";

        /*比较常用的两个 sadd srem smembers*/
        //向名称为key的set中添加元素value1,value2
        jedis.sadd(key,"value1","value2");
        //删除名称为key的set中的元素member
        jedis.srem(key,"value1");
        //返回名称为key的set的所有元素
        Set<String> set=jedis.smembers(key);
        
       boolean flag=jedis.sismember(key,"value2");

        /*随机返回指定set的中的一个值，一个进行删除 一个不进行删除*/
        //随机返回并删除名称为key的set中一个元素
        String randomDelValue = jedis.spop(key);
        //随机返回名称为key的set的一个元素
        String randomValue =jedis.srandmember(key);

        /**这个方法比较好，就是将seta 里面的某个值 转移到setb.  如果seta 里面有这个值 就会删除seta 里面这个值，如果setb里面没有这个值，就会新增一个值，如果有就不会发生变动**/
        jedis.smove(key,key2,"value1");

        //求并集
        Set<String> sets = jedis.sunion(key, key2);
        //交集
        Set<String> setss=jedis.sinter(key,key2);
    }

}
