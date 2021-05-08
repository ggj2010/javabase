package netty.second.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 自定义解码器
 * @author gaoguangjin
 */
@Slf4j
public class MyClientDecoder extends ByteToMessageDecoder {
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] bytes=new byte[in.readableBytes()];
        in.readBytes(bytes);
        log.info("decode:"+new String(bytes,"utf-8"));
        out.add(bytes);
    }

}
