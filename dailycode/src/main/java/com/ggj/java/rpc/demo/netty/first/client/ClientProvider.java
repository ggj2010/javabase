package com.ggj.java.rpc.demo.netty.first.client;

import com.ggj.java.rpc.demo.netty.first.client.handle.ClientDecoder;
import com.ggj.java.rpc.demo.netty.first.client.handle.ClientEncoder;
import com.ggj.java.rpc.demo.netty.first.client.handle.RpcClientHandler;
import com.ggj.java.rpc.demo.netty.first.server.handle.ServerDecoder;
import com.ggj.java.rpc.demo.netty.first.server.handle.ServerEncoder;
import com.ggj.java.rpc.demo.netty.first.vo.RpcRequest;
import com.ggj.java.rpc.demo.netty.first.vo.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gaoguangjin
 */
@Slf4j
public class ClientProvider {
    private static Map<String, RpcInvokeFutureResult> rpcResultMap = new ConcurrentHashMap<>();

    private static ChannelFuture channelFuture = null;

    static {
        init();
    }

    private static void init() {
        startNettyClient();
    }

    private static void startNettyClient() {
        Bootstrap clentBootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        clentBootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        /**
                         /**自定义协议解决粘包 begin**/
                        ch.pipeline().addLast(new ClientEncoder());
                        ch.pipeline().addLast(new ClientDecoder());
                        /**自定义协议解决粘包 end**/
                        ch.pipeline().addLast(new RpcClientHandler());
                    }
                }).option(ChannelOption.SO_KEEPALIVE, true);
        try {
            channelFuture = clentBootstrap.connect("127.0.0.1", 8000).sync();
        } catch (InterruptedException e) {
            log.error("init netty client error", e);
        }
    }

    public static ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    /**
     * 发送消息和返回future结果
     *
     * @param
     * @param requestId
     * @return
     */
    public static RpcInvokeFutureResult sendAndGetRpcInvokeFutureResult(RpcRequest rpcRequest, String requestId) {
        RpcInvokeFutureResult rpcInvokeFutureResult = new RpcInvokeFutureResult(requestId,rpcResultMap);
        getChannelFuture().channel().writeAndFlush(rpcRequest);
        ClientProvider.rpcResultMap.put(requestId, rpcInvokeFutureResult);
        return rpcInvokeFutureResult;
    }


    public static void putRpcResponse(RpcResponse response) {
        RpcInvokeFutureResult rpcInvokeFutureResult = rpcResultMap.get(response.getRequestId());
        if (rpcInvokeFutureResult != null) {
            rpcInvokeFutureResult.put(response);
        }
    }
}
