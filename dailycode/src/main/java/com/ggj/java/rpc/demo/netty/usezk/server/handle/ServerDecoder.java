package com.ggj.java.rpc.demo.netty.usezk.server.handle;

import com.ggj.java.rpc.demo.netty.usezk.util.Tool;
import com.ggj.java.rpc.demo.netty.usezk.vo.RpcRequest;
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
public class ServerDecoder extends ByteToMessageDecoder {
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //最小可读长度要大于4
        int readableBytes = in.readableBytes();
        if (readableBytes < 4) {
            return;
        }
        //标记当前指针位置
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0) {
            ctx.close();
        }
        //如果数据可读长度小于发送来读数据包总长度，丢弃这次读取
        if (readableBytes < dataLength) {
            //恢复读指针到原来的位置
            in.resetReaderIndex();
            return;
        }
        //开始读啦
        byte[] bytes = new byte[dataLength - 4];
        in.readBytes(bytes);
        //反序列化获取request
//        out.add(KryoUtil.deserialize(bytes, RpcRequest.class));
        out.add(Tool.deserialize(bytes, RpcRequest.class));
    }
}
