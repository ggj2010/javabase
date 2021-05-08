package com.ggj.java.rpc.demo.netty.usezk.client.handle;
import com.ggj.java.rpc.demo.netty.usezk.util.Tool;
import com.ggj.java.rpc.demo.netty.usezk.vo.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 解决拆包粘包
 *
 * @author gaoguangjin
 */
@Slf4j
public class    ClientDecoder extends ByteToMessageDecoder {
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        int readableBytes = in.readableBytes();
        //最小可读长度要大于4
        if (readableBytes < 4) {
            return;
        }
        //标记当前指针位置
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0) {
            ctx.close();
        }
        //如果数据可读长度小于发送来读数据包总长度，当然不能继续读
        if (readableBytes < dataLength) {
            //恢复读指针到原来的位置
            in.resetReaderIndex();
            return;
        }
        //开始读啦
        byte[] bytes = new byte[dataLength - 4];
        in.readBytes(bytes);

        //反序列请求
        out.add(Tool.deserialize(bytes, RpcResponse.class));
    }

}
