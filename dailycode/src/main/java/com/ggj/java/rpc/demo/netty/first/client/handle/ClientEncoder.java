package com.ggj.java.rpc.demo.netty.first.client.handle;

import com.ggj.java.rpc.demo.netty.first.util.Tool;
import com.ggj.java.serializer.KryoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;


/**
 * 解决拆包粘包
 *
 * @author gaoguangjin
 */
@Slf4j
public class ClientEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        try {
//          byte[] responseByte = KryoUtil.serialiaztion(msg);
            byte[] responseByte = Tool.serialize(msg);
            int totalLength = 4 + responseByte.length;
            out.writeInt(totalLength);
            out.writeBytes(responseByte);
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
