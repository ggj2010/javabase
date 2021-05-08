package com.ggj.java.rpc.demo.benchmark;

import com.ggj.java.rpc.demo.first.TestService;
import com.ggj.java.rpc.demo.second.ConsumerClient;
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
import java.util.concurrent.TimeUnit;

/**
 * @author gaoguangjin
 */
@State(Scope.Benchmark)
public class SecondBenchMarkTest extends AbstractBenchMarkTest{
    private TestService testService;

    @Setup(Level.Trial)
    @Override
    public void init() {
        testService = ConsumerClient.getProxyClass(TestService.class);
    }
    /**
     * Benchmark                        Mode  Cnt    Score   Error  Units
     * SecondBenchMarkTest.testMethod  thrpt    2  740.836          ops/s
     * @param args
     * @throws RunnerException
     */
    public static void main(String[] args) throws RunnerException {
        SecondBenchMarkTest secondBenchMarkTest=new SecondBenchMarkTest();
        new Runner(secondBenchMarkTest.build(SecondBenchMarkTest.class.getSimpleName())).run();
    }

    @Benchmark
    /**
     * Throughput	每段时间执行的次数，一般是秒
     * AverageTime	平均时间，每次操作的平均耗时
     * SampleTime	在测试中，随机进行采样执行的时间
     * SingleShotTime	在每次执行中计算耗时
     */
    @BenchmarkMode({ Mode.All})
   // @BenchmarkMode(Mode.Throughput)
    //毫秒纬度统计
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void testMethod() {
      testService.testMethod("clientone");
    }


}
