package com.ggj.java.rpc.demo.spring.firstdemo.rpcserver.netty;

import com.ggj.java.rpc.demo.netty.usezk.server.handle.RpcServerHandler;
import com.ggj.java.rpc.demo.netty.usezk.server.handle.ServerDecoder;
import com.ggj.java.rpc.demo.netty.usezk.server.handle.ServerEncoder;
import com.ggj.java.rpc.demo.netty.usezk.util.Constants;
import com.ggj.java.rpc.demo.spring.firstdemo.AbstractServer;
import com.ggj.java.rpc.demo.spring.firstdemo.common.domain.Disposable;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author gaoguangjin
 */
@Slf4j
public class NettyServer extends AbstractServer implements Disposable {

    private volatile boolean started;

    private ServerBootstrap serverBootstrap;
    private Channel channel;
    private ChannelFuture channelFuture;

    @Override
    public void doStart() {
        if (!started) {
            checkPortBind();
            try {
                channelFuture = this.serverBootstrap.bind(Constants.HOST_ADRESS, Constants.DEFAULT_SERVER_PORT).sync();
            } catch (InterruptedException e) {
                log.error("", e);
            }
            channel = channelFuture.channel();
            started = true;
        }
    }

    public NettyServer() {
        serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        serverBootstrap.group(boosGroup, workGroup).channel(NioServerSocketChannel.class).childOption(ChannelOption.SO_KEEPALIVE, true).childOption(ChannelOption.TCP_NODELAY, true).childOption(ChannelOption.SO_REUSEADDR, true).childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
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
                });
    }

    private void checkPortBind() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(Constants.DEFAULT_SERVER_PORT);
        } catch (Exception e) {
            log.error("unable to start netty server on port " + Constants.DEFAULT_SERVER_PORT
                    + ", the port is in use");
            System.exit(0);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                log.debug("ignore", e);
            }
        }
    }

    @Override
    public void doStop() {
        if (this.started) {
            try {
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                log.warn("close error");
            }
            this.started = false;
        }
    }

    @Override
    public void destory() throws Exception {
        stop();
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public int getPort() {
        return Constants.DEFAULT_SERVER_PORT;
    }

    @Override
    public String getRegistryUrl() {
        return Constants.HOST_ADRESS;
    }
}
