package com.ggj.java.rpc.demo.netty.usezk.server.handle;

import com.ggj.java.rpc.demo.netty.usezk.server.ServerProvider;
import com.ggj.java.rpc.demo.netty.usezk.vo.RpcRequest;
import com.ggj.java.rpc.demo.netty.usezk.vo.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gaoguangjin
 */
@Slf4j
public class RpcServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest rpcRequest = (RpcRequest) msg;

        RpcResponse response=new RpcResponse();
        response.setRequestId(rpcRequest.getRequestId());
        String requestClassName = rpcRequest.getClassName();
        Object object = ServerProvider.cacheServiceMap.get(requestClassName);
        try {
            Object result = object.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes()).invoke(object, rpcRequest.getParameters());
            response.setResponse(result);
        }catch (Throwable e){
            response.setExcetion(e.getMessage());
            log.error("",e);
        }
        ctx.writeAndFlush(response);
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
