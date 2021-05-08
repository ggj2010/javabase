package com.ggj.java.distributedtask.core.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Random;

public class LocalHostService {


	private static volatile String cachedIpAddress;

	public static InetAddress getLocalHost() {
		try {
			InetAddress result = InetAddress.getLocalHost();
			return result;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) throws UnknownHostException, SocketException {
		System.out.println(new LocalHostService().getIp());
	}

	/**
	 * 获取本机IP地址.
	 * <p>
	 * <p>
	 * 有限获取外网IP地址.
	 * 也有可能是链接着路由器的最终IP地址.
	 * </p>
	 *
	 * @return 本机IP地址
	 */
	public String getIp() {
		if (null != cachedIpAddress) {
			return cachedIpAddress;
		}
		try {
			Enumeration<NetworkInterface> netInterfaces;
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			String localIpAddress = null;
			while(netInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = netInterfaces.nextElement();
				Enumeration<InetAddress> ipAddresses = netInterface.getInetAddresses();
				while(ipAddresses.hasMoreElements()) {
					InetAddress ipAddress = ipAddresses.nextElement();
					if (isPublicIpAddress(ipAddress)) {
						String publicIpAddress = ipAddress.getHostAddress();
						cachedIpAddress = publicIpAddress;
						return publicIpAddress;
					}
					if (isLocalIpAddress(ipAddress)) {
						localIpAddress = ipAddress.getHostAddress();
					}
				}
			}
			Random random=new Random();
			random.nextInt(10000000);
			cachedIpAddress = localIpAddress+random.nextInt(10000000);
			return cachedIpAddress;
		} catch (SocketException e) {
			e.printStackTrace();
			return null;
		}
	}

	private boolean isPublicIpAddress(final InetAddress ipAddress) {
		return !ipAddress.isSiteLocalAddress() && !ipAddress.isLoopbackAddress() && !isV6IpAddress(ipAddress);
	}

	private boolean isLocalIpAddress(final InetAddress ipAddress) {
		return ipAddress.isSiteLocalAddress() && !ipAddress.isLoopbackAddress() && !isV6IpAddress(ipAddress);
	}

	private boolean isV6IpAddress(final InetAddress ipAddress) {
		return ipAddress.getHostAddress().contains(":");
	}

	/**
	 * 获取本机Host名称.
	 *
	 * @return 本机Host名称
	 */
	public String getHostName() throws UnknownHostException {
		return getLocalHost().getHostName();
	}
}
