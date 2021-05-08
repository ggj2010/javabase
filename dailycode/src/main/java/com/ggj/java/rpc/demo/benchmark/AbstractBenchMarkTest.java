package com.ggj.java.rpc.demo.benchmark;

import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

/**
 *  * 参考：https://www.jianshu.com/p/0da2988b9846
 * @author gaoguangjin
 */
public abstract class AbstractBenchMarkTest {

    public abstract void init();

    public Options build(String className) {
        return new OptionsBuilder()
                .include(className)
                //预热
                .warmupIterations(1)
                .warmupTime(TimeValue.seconds(10))
                //正式计量
                .measurementIterations(2)
                .measurementTime(TimeValue.seconds(10))
                //需要和server端的最大线程数一致
                .threads(Runtime.getRuntime().availableProcessors())
                //只做1轮测试
                .forks(1)
                .build();
    }
}
