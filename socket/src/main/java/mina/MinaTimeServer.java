package mina;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class MinaTimeServer {
	private static final int PORT = 9123;// 定义监听端口
	
	public static void main(String[] args) throws IOException {
		IoAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));// 指定编码过滤器
		acceptor.setHandler(new TimeServerHandler());// 指定业务逻辑处理器

		//读写 通道均在3 秒内无任何操作就进入空闲状态
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 3);

		acceptor.setDefaultLocalAddress(new InetSocketAddress(PORT));// 设置端口号
		acceptor.bind();// 启动监听
	}
}
