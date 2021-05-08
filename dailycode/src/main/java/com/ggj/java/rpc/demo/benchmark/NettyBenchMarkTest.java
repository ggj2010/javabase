package com.ggj.java.rpc.demo.benchmark;

import com.ggj.java.rpc.demo.first.TestService;
import com.ggj.java.rpc.demo.netty.first.client.RpcInvocationHandler;
import com.ggj.java.rpc.demo.netty.first.server.service.AppleService;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;

import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * 需要先启动com.ggj.java.rpc.demo.netty.first.server.ServerProvider
 * @author gaoguangjin
 */
//每个测试线程分配一个实例
@State(Scope.Benchmark)
public class NettyBenchMarkTest  extends AbstractBenchMarkTest{
    private AppleService appleService;

    @Setup(Level.Trial)
    @Override
    public void init() {
        appleService= (AppleService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class<?>[]{AppleService.class} ,new RpcInvocationHandler());
    }

    /**
     * Benchmark                       Mode  Cnt      Score   Error  Units
     * NettyBenchMarkTest.testMethod  thrpt    2  60971.413          ops/s
     */
    public static void main(String[] args) throws RunnerException {
        NettyBenchMarkTest nettyBenchMarkTest=new NettyBenchMarkTest();
        new Runner(nettyBenchMarkTest.build(NettyBenchMarkTest.class.getSimpleName())).run();
    }

    //默认level。全部benchmark运行(一组迭代)之前/之后
    @TearDown(Level.Trial)
    public void arrayRemove() {
    }

    @Benchmark
    /**
     * Throughput	每段时间执行的次数，一般是秒
     * AverageTime	平均时间，每次操作的平均耗时
     * SampleTime	在测试中，随机进行采样执行的时间
     * SingleShotTime	在每次执行中计算耗时
     */
    @BenchmarkMode({ Mode.Throughput})
    //毫秒纬度统计
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void testMethod() {
        appleService.getApplePrice(1);
    }

}
