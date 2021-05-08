package com.ggj.java.rpc.demo.netty.usezk.server.handle;

import com.ggj.java.rpc.demo.netty.usezk.util.Tool;
import com.ggj.java.rpc.demo.netty.usezk.vo.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;


/**
 * 解决拆包粘包
 * @author gaoguangjin
 */
@Slf4j
public class ServerEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        try {
            //序列化请求
            byte[] responseByte = Tool.serialize(msg);
            //总长度
            int totalLength=4+responseByte.length;
            out.writeInt(totalLength);
            out.writeBytes(responseByte);
            //log.info("callback");
        }catch (Exception e){
            log.error("",e);
        }
    }
}
