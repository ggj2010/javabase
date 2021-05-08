package netty.fisrtdemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gaoguangjin
 */
public class ResponseHandler extends SimpleChannelInboundHandler<String> {
    AtomicInteger atomicInteger=new AtomicInteger();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("receive msg:"+msg);
        //ctx的writeAndFlush是从当前handler直接发出这个消息，
        ctx.write("response"+atomicInteger.getAndIncrement());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

}
