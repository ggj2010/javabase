package com.ggj.java.rpc.demo.netty.first.client.handle;

import com.ggj.java.rpc.demo.netty.first.client.ClientProvider;
import com.ggj.java.rpc.demo.netty.first.server.ServerProvider;
import com.ggj.java.rpc.demo.netty.first.vo.RpcRequest;
import com.ggj.java.rpc.demo.netty.first.vo.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gaoguangjin
 */
@Slf4j
public class RpcClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcResponse rpcResponse = (RpcResponse) msg;
        ClientProvider.putRpcResponse(rpcResponse);
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
