package netty.second.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.second.server.MyServerFixPacketDecoder;

/**
 * @author gaoguangjin
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
//                        ch.pipeline().addLast(new MyClientDecoder());
                        //ch.pipeline().addLast(new MyClientEncoder());
                        /**自定义协议解决粘包 begin**/
                        ch.pipeline().addLast(new MyClientFixPacketEncoder());
                        ch.pipeline().addLast(new MyServerFixPacketDecoder());
                        /**自定义协议解决粘包 end**/
                        //处理
                        ch.pipeline().addLast(new MyClientHandler());
                    }
                })  .option(ChannelOption.SO_KEEPALIVE, true);
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8000).sync();
        Channel channel = channelFuture.channel();
        // 会有粘包问题
        for (int i=0;i<=10;i++) {
            channel.writeAndFlush(String.valueOf(i));
            //可以表面看不到粘包现象
            //Thread.sleep(2000);
        }
    }
}
