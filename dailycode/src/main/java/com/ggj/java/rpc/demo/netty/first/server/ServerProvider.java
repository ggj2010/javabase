package com.ggj.java.rpc.demo.netty.first.server;

import com.ggj.java.rpc.demo.netty.first.server.annation.RpcService;
import com.ggj.java.rpc.demo.netty.first.server.handle.RpcServerHandler;
import com.ggj.java.rpc.demo.netty.first.server.handle.ServerDecoder;
import com.ggj.java.rpc.demo.netty.first.server.handle.ServerEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author gaoguangjin
 */
@Slf4j
public class ServerProvider {
    public static Map<String, Object> cacheSericeMap = new HashMap<>();
    private static AtomicBoolean initStatus = new AtomicBoolean(false);

    public static void init() {
        if (initStatus.compareAndSet(false, true)) {
            scannAndCacheService();
            startNettyServer();
        } else {
            throw new IllegalStateException("can not repeat init");
        }
    }

    private static void startNettyServer() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        serverBootstrap.group(boosGroup, workGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        /**
                         /**自定义协议解决粘包 begin**/
                        ch.pipeline().addLast(new ServerEncoder());
                        ch.pipeline().addLast(new ServerDecoder());
                        /**自定义协议解决粘包 end**/
                        ch.pipeline().addLast(new RpcServerHandler());
                    }
                }).bind(8000);
    }

    public static void main(String[] args) {
        init();
    }

    /**
     * 扫描指定目录文件
     */
    private static void scannAndCacheService() {
        try {
            log.info("scann rpc class begin");
            ResourcePatternResolver rp = new PathMatchingResourcePatternResolver();
            Resource[] resources = rp.getResources("classpath:com/ggj/java/rpc/demo/netty/first/server/service/imp/*.class");
            if (resources == null || resources.length == 0) {
                throw new IllegalArgumentException("scann package error");
            }
            for (Resource resource : resources) {
                String className = resource.getFile().getPath().split("classes\\/")[1].replaceAll("\\/", ".").replaceAll(".class", "");
                Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
                if (clazz.getAnnotation(RpcService.class) != null) {
                    Object object = clazz.newInstance();
                    cacheSericeMap.putIfAbsent(object.getClass().getInterfaces()[0].getName(), object);
                }
            }
            log.info("scann rpc class end");
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
