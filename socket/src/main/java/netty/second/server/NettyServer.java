package netty.second.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import netty.second.client.MyClientFixPacketEncoder;

/**
 * @author gaoguangjin
 */
public class NettyServer {

    /**
     * Encoder 先注册的后执行，与OutboundHandler一致；Decoder是先注册的先执行，与InboundHandler一致。
     * @param args
     */
    public static void main(String[] args) {
        ServerBootstrap serverBootstrap=new ServerBootstrap();
        NioEventLoopGroup boosGroup=new NioEventLoopGroup();
        NioEventLoopGroup workGroup=new NioEventLoopGroup();

        serverBootstrap.group(boosGroup,workGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        /**
                         * 服务端先encoder 再  decode
                         */
                        //out 才encoder
//                        ch.pipeline().addLast(new MyServerEncoderTwo());
                        //ch.pipeline().addLast(new MyServerDecoder());
                        //in decoder
                        /**自定义协议解决粘包 begin**/
                        ch.pipeline().addLast(new MyClientFixPacketEncoder());
                        ch.pipeline().addLast(new MyServerFixPacketDecoder());
                        /**自定义协议解决粘包 end**/
                        ch.pipeline().addLast(new MyServerHandler());
                    }
                }).bind(8000);
    }
}
