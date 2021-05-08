package com.ggj.java.rpc.demo.spring.firstdemo.rpcserver;

import com.ggj.java.rpc.demo.spring.firstdemo.common.extention.ExtensionLoader;
import com.ggj.java.rpc.demo.spring.firstdemo.rpcserver.Listener.ShutDownHookListener;
import com.ggj.java.rpc.demo.spring.firstdemo.rpcserver.config.ProviderConfig;

import java.util.List;

/**
 * @author gaoguangjin
 */
public class ProviderBootStrap {
    private static volatile boolean started;

    public static void init() {
        if (!started) {
            synchronized (ProviderBootStrap.class) {
                if (!started) {
                    //init other code
                    Thread shutdownHook = new Thread(new ShutDownHookListener());
                    shutdownHook.setDaemon(true);
                    shutdownHook.setPriority(Thread.MAX_PRIORITY);
                    Runtime.getRuntime().addShutdownHook(shutdownHook);
                    List<Server> servers = ExtensionLoader.getExtensionList(Server.class);
                    for (Server server : servers) {
                        if (!server.isStarted()) {
                            server.start();
                        }
                    }
                    started = true;
                }
            }
        }
    }

    public static void startup() {
    }

    public static void shutDown() {
        if (started) {
            //TODO ....
            started=false;
        }
    }

    public static List<Server> getServers() {
        return ExtensionLoader.getExtensionList(Server.class);
    }
}
