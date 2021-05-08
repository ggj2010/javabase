package netty.second.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;


/**
 * 自定义解码器
 * @author gaoguangjin
 */
@Slf4j
public class MyServerEncoderTwo extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
       log.info("encode two:{}",msg);
        try {
            String message = (String) msg;
            out.writeBytes(message.getBytes());
        }catch (Exception e){
            log.error("",e);
        }
    }
}
