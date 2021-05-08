package com.ggj.java.rpc.demo.netty.usezk.client;

import com.ggj.java.rpc.demo.netty.usezk.client.handle.ClientDecoder;
import com.ggj.java.rpc.demo.netty.usezk.client.handle.ClientEncoder;
import com.ggj.java.rpc.demo.netty.usezk.client.handle.RpcClientHandler;
import com.ggj.java.rpc.demo.netty.usezk.register.RegisterManager;
import com.ggj.java.rpc.demo.netty.usezk.util.Constants;
import com.ggj.java.rpc.demo.netty.usezk.vo.RpcRequest;
import com.ggj.java.rpc.demo.netty.usezk.vo.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author gaoguangjin
 */
@Slf4j
public class ClientProvider {
    @Getter
    private static Map<String, RpcInvokeFutureResult> rpcResultMap = new ConcurrentHashMap<>();
    @Getter
    //className -> ipList
    private static Map<String, ChannelFutureHolder> clientConnectMap = new ConcurrentHashMap();
    @Getter
    // ip-> 多个链接
    private static Map<String, List<ChannelFuture>> channelFutureConnectMap = new ConcurrentHashMap();
    private static Lock lock = new ReentrantLock();

    public static ChannelFuture getNettyClient(String ipStrs, String className) throws Exception {
        lock.lock();
        try {
            if (clientConnectMap.containsKey(className)) {
                return clientConnectMap.get(className).selectClient();
            }
            String[] ipArray = ipStrs.split("\\,");
            List<String> ipList = new ArrayList<>();
            for (String iparray : ipArray) {
                initClient(iparray);
                ipList.add(iparray);
            }
            clientConnectMap.put(className, new ChannelFutureHolder(ipList));
        } catch (Exception e) {
            log.error("init netty client error", e);
            throw e;
        } finally {
            lock.unlock();
        }
        return clientConnectMap.get(className).selectClient();
    }

    public static List<ChannelFuture> initClient(String ip) throws InterruptedException {
        lock.lock();
        List<ChannelFuture> futureList = new ArrayList<>();
        try {
            String[] ipArray = ip.split("\\:");
            if (channelFutureConnectMap.containsKey(ip)) {
                return channelFutureConnectMap.get(ip);
            }
            for (int i = 0; i < Constants.CLIENT_INIT_CONNECTION_SIZE; i++) {
                Bootstrap clentBootstrap = new Bootstrap();
                NioEventLoopGroup group = new NioEventLoopGroup();
                clentBootstrap.group(group).channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new ClientEncoder());
                                ch.pipeline().addLast(new ClientDecoder());
                                ch.pipeline().addLast(new RpcClientHandler());
                            }
                        }).option(ChannelOption.SO_KEEPALIVE, true);
                ChannelFuture channelFuture = clentBootstrap.connect(ipArray[0], Integer.parseInt(ipArray[1])).sync();
                futureList.add(channelFuture);
            }
            channelFutureConnectMap.put(ip, futureList);
        } catch (Exception e) {
            log.error("init netty client error", e);
            throw e;
        } finally {
            lock.unlock();
        }
        return futureList;
    }


    public static Map<String, ChannelFutureHolder> getClientConnectMap() {
        return clientConnectMap;
    }

    /**
     * 1、缓存map 是否存在连接
     * 如果存在 判断是否active
     * 如果不active 就切换到其他到
     *
     * @param rpcRequest
     * @return
     * @throws Exception
     */
    public static ChannelFuture getChannelFuture(RpcRequest rpcRequest) throws Exception {
        ChannelFuture channelFuture = null;
        String className = rpcRequest.getClassName();

        if (clientConnectMap.containsKey(className)) {
            channelFuture = clientConnectMap.get(className).selectClient();
            if (channelFuture != null) {
                return channelFuture;
            }
        }
        String iplist = getConnectIp(className);
        if (StringUtils.isEmpty(iplist)) {
            throw new Exception(className + " has no availiable server");
        }
        channelFuture = getNettyClient(iplist, className);
        return channelFuture;
    }

    /**
     * 获取服务端ip
     *
     * @param className
     * @return
     * @throws Exception
     */
    private static String getConnectIp(String className) throws Exception {
        return RegisterManager.getClientInstance().getRegister().getServiceAddress(className);
    }

    /**
     * 发送消息和返回future结果
     *
     * @param
     * @param requestId
     * @return
     */
    public static RpcInvokeFutureResult sendAndGetRpcInvokeFutureResult(RpcRequest rpcRequest, String requestId) throws Exception {
        RpcInvokeFutureResult rpcInvokeFutureResult = new RpcInvokeFutureResult(requestId, rpcResultMap);
        Channel channel = getChannelFuture(rpcRequest).channel();
        ChannelFuture channelFuture = channel.writeAndFlush(rpcRequest);
        //监听resposne返回结果,如果服务端下线会收到通知
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture rfuture) throws Exception {
                if (!rfuture.isSuccess()) {
                    log.info("rpc rsponse unsuccessful");
                    rpcInvokeFutureResult.setCause(rfuture.cause());
                }
            }
        });
        ClientProvider.rpcResultMap.put(requestId, rpcInvokeFutureResult);
        return rpcInvokeFutureResult;
    }


    public static void putRpcResponse(RpcResponse response) {
        RpcInvokeFutureResult rpcInvokeFutureResult = rpcResultMap.get(response.getRequestId());
        if (rpcInvokeFutureResult != null) {
            rpcInvokeFutureResult.put(response);
        }
    }

    @Data
    public static class ChannelFutureHolder {

        private AtomicInteger atomicInteger = new AtomicInteger(1);
        private String ip;
        @Getter
        List<String> ipList;

        public ChannelFutureHolder(List<String> ipList) {
            this.ipList = ipList;
        }

        /**
         * 随机选服务端ip
         * 随机选服务端的连接
         *
         * @return
         */
        public ChannelFuture selectClient() {
            String randomIp = ipList.get(new Random().nextInt(ipList.size()));
            //System.out.println("invoke ip="+randomIp);
            List<ChannelFuture> channelFutureList = channelFutureConnectMap.get(randomIp);
            if (CollectionUtils.isEmpty(channelFutureList)) {
                return null;
            }
            return channelFutureList.get(atomicInteger.getAndIncrement() % Constants.CLIENT_INIT_CONNECTION_SIZE);
        }
    }
}
