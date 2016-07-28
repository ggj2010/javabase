package com.ggj.java;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * @author:gaoguangjin
 * @date 2016/7/15 13:42
 */
public class RedisCluster {

    /**
     * 如果配置了ip 那么redis的配置文件里面的redis-config的 bind 需要修改成指定的ip
     * redis-trib.rb  create --replicas 1  110.173.28.188:7000 110.173.28.188:7001 110.173.28.188:7002 110.173.28.188:7003 110.173.28.188:7004 110.173.28.188:7005
     * @param args
     */
    public static void main(String[] args) {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("110.173.28.188", 7000));
        jedisClusterNodes.add(new HostAndPort("110.173.28.188", 7001));
        jedisClusterNodes.add(new HostAndPort("110.173.28.188", 7002));
        JedisCluster jedisCluster=new JedisCluster(jedisClusterNodes);

        jedisCluster.set("master1","1");
        jedisCluster.set("master2","2");
        jedisCluster.set("master3","3");
    }


}
