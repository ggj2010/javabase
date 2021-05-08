package netty.fisrtdemo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author gaoguangjin
 */
@Slf4j
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        //out
                        ch.pipeline().addLast(new StringDecoder());
                        //in 处理
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new RequestHandler());
                        ch.pipeline().addLast(new IdleStateHandler(0, 0, 5));

                    }
                });
        Channel channel = bootstrap.connect("127.0.0.1", 8000).channel();
        int i = 0;
        while (true) {

            //channel的writeAndFlush是从整个pipline最后一个outhandler发出
            ChannelFuture channelFuture = channel.writeAndFlush(i + ": hello world!");
            try {
            } catch (Exception e) {
                log.error("{}", e);
            }
            i++;
            Thread.sleep(1000);
            log.info("active :" + channel.isActive());
        }
    }
}
