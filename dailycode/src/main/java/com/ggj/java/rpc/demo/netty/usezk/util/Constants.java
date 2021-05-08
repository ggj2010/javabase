package com.ggj.java.rpc.demo.netty.usezk.util;


/**
 * @author gaoguangjin
 */
public class Constants {

    public static final String DEFAULT_CONNECT_URL = "localhost:2181";
    public static final String APP_NAME_KEY = "appName";
    public static final String NAME_SPACE = "ggjRPC";
    public static final String SERVER_PATH = "/server";
    public static final String APP_PATH = "/app";
    public static final String WEIGHT_PATH = "/weight";
    public static final int DEFAULT_SERVER_PORT =Integer.parseInt(System.getProperty("port")==null?"4081":System.getProperty("port"));
    public static final int DEFAULT_ONLINE_WEIGHT = 1;
    public static final int DEFAULT_OFFLINE_WEIGHT = -1;
    public static final String HOST_ADRESS=LocalHostService.getLocalHost().getHostAddress();
    public static final String SERVICE_ADRESS = HOST_ADRESS+ ":" +DEFAULT_SERVER_PORT;

    //客户端和每个服务端建立两个连接
    //public static final int CLIENT_INIT_CONNECTION_SIZE=Runtime.getRuntime().availableProcessors()/3 -2;;
    public static final int CLIENT_INIT_CONNECTION_SIZE=2;;

}
