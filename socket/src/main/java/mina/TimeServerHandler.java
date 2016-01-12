package mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import java.util.Date;

public class TimeServerHandler extends IoHandlerAdapter {
	public void sessionCreated(IoSession session) {
		// 显示客户端的ip和端口
		System.out.println("调用继承IoHandlerAdapter类的sessionCreated()方法" + session.getRemoteAddress().toString());
	}
	
	public void messageReceived(IoSession session, Object message) throws Exception {
		String str = message.toString();
		
		System.out.println("接收到的str:" + str);
		if (str.trim().equalsIgnoreCase("quit")) {
			session.close();// 结束会话
			return;
		}
		Date date = new Date();
		session.write(date.toString());// 返回当前时间的字符串
		System.out.println("Message written...");
	}

}