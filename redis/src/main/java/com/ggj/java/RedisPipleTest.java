package com.ggj.java;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis hgetall有性能问题
 * @author:gaoguangjin
 * @date 2016/8/31 11:19
 */
public class RedisPipleTest {
    private static Jedis jedis = new Jedis("110.173.28.188");

    public static void main(String[] args) {
        jedis.auth("gaoguangjin");
        jedis.select(2);
//        Pipeline p = jedis.pipelined();
//        for(int i=0;i<3000;i++){
//            p.set("a"+i,i+"");
//        }
//        p.sync();

        long beginTime=System.currentTimeMillis();
//        Pipeline p = jedis.pipelined();
//        Response<Map<String, String>> dd = p.hgetAll("tieba_content_image_黄河科技学院");
//        p.sync();
//        Map<String, String> map=dd.get();
        Set<String> key = jedis.hkeys("tieba_content_image_黄河科技学院");
        System.out.println("耗时："+(System.currentTimeMillis()-beginTime)+"s");
        Pipeline p = jedis.pipelined();
        List<Response<String>> lis=new ArrayList<>();
        for (String s : key) {
            beginTime=System.currentTimeMillis();
            lis.add( p.hget("tieba_content_image_黄河科技学院", s));
            System.out.println("耗时："+(System.currentTimeMillis()-beginTime)+"s");
        }
        p.sync();
//        List<Response<String>> lis=new ArrayList<>();
//        Pipeline p = jedis.pipelined();
//        for(int i=0;i<3000;i++){
//            lis.add( p.get("a" + i));
//        }
//        p.sync();
        System.out.println(lis.size()+"耗时："+(System.currentTimeMillis()-beginTime)+"s");
    }
}
