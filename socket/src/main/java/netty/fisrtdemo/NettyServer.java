package netty.fisrtdemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author gaoguangjin
 */
public class NettyServer {
    public static void main(String[] args) {
        //bossGroup 用于服务端接受客户端连接，
        // workerGroup 用于进行 SocketChannel 网络读写
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            serverBootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            //out
                            ch.pipeline().addLast(new StringEncoder());

                            //in
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new ResponseHandler());
                        }
                    })
                    .bind(8000);
        } catch (Exception e) {

        } finally {
            /**优雅退出，释放线程池资源*/
           // bossGroup.shutdownGracefully();
            //workerGroup.shutdownGracefully();
        }
    }
}
