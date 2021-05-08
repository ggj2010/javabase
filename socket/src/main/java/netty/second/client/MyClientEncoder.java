package netty.second.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;


/**
 * 自定义加码器
 * @author gaoguangjin
 */
@Slf4j
public class MyClientEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        try {
            String message = (String) msg;
            out.writeInt(10);
            out.writeBytes(message.getBytes());
        }catch (Exception e){
            log.error("",e);
        }
    }
}
