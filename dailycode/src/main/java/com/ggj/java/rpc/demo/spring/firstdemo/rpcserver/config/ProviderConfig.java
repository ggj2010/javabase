package com.ggj.java.rpc.demo.spring.firstdemo.rpcserver.config;

/**
 * @author gaoguangjin
 */
public class ProviderConfig<T> {
    public static final int DEFAULT_HTTP_PORT = 4080;

    private Class<T> interfaceClass;
    private String url;
    private String version;
    private String group;
    private int port = DEFAULT_HTTP_PORT;
    private T service;

    public ProviderConfig(Class<T> interfaceClass, T service) {
        if (!interfaceClass.isInstance(service)) {
            throw new IllegalArgumentException("Service interface [" + interfaceClass.getName() + "] need to be implemented by service [" + service + "] of class[" + service.getClass().getName() + "]");
        }
        this.interfaceClass = interfaceClass;
        this.service = service;
    }


    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getPort() {
        return port;
    }

    public T getService() {
        return service;
    }

    public void setService(T service) {
        this.service = service;
    }
}
