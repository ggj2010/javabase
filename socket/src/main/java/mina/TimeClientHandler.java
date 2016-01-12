package mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class TimeClientHandler extends IoHandlerAdapter {
	public TimeClientHandler() {
	}
	
	public void messageReceived(IoSession session, Object message) throws Exception {
		System.out.println("【服务端回复：这是时间：】" + message + "收到的消息client");// 显示接收到的消息
	}
}
