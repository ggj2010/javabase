package com.ggj.java.rpc.demo.benchmark;

import com.ggj.java.rpc.demo.netty.usezk.client.RpcInvocationHandler;
import com.ggj.java.rpc.demo.netty.usezk.server.service.AppleService;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;

import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * @author gaoguangjin
 */
@Slf4j
@State(Scope.Benchmark)
public class NettyUseZookeeperBenchMarkTest extends AbstractBenchMarkTest{
    private AppleService appleService;
    @Setup(Level.Trial)
    @Override
    public void init() {
        appleService = (AppleService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{AppleService.class}, new RpcInvocationHandler());
    }

    /**
     * Benchmark                                   Mode  Cnt      Score   Error  Units
     * NettyUseZookeeperBenchMarkTest.testMethod  thrpt    2  52227.395          ops/s
     * @param args
     * @throws RunnerException
     */
    public static void main(String[] args) throws RunnerException {
        NettyUseZookeeperBenchMarkTest nettyUseZookeeperBenchMarkTest =new NettyUseZookeeperBenchMarkTest();
        new Runner(nettyUseZookeeperBenchMarkTest.build(NettyUseZookeeperBenchMarkTest.class.getSimpleName())).run();
    }


    @Benchmark
    /**
     * Throughput	每段时间执行的次数，一般是秒
     * AverageTime	平均时间，每次操作的平均耗时
     * SampleTime	在测试中，随机进行采样执行的时间
     * SingleShotTime	在每次执行中计算耗时
     */
    @BenchmarkMode({ Mode.Throughput})
   // @BenchmarkMode(Mode.Throughput)
    //毫秒纬度统计
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void testMethod() {
        try {
            String result = appleService.getApplePrice(100);
        }catch (Exception e){
           //log.error("",e);
        }

    }


}
