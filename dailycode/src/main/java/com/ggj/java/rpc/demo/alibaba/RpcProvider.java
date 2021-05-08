package com.ggj.java.rpc.demo.alibaba;

/**
 * @author gaoguangjin
 */
public class RpcProvider {
    public static void main(String[] args) throws Exception {
        HelloService service = new HelloServiceImpl();
        RpcFramework.export(service, 1234);
    }
}
