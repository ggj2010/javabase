package com.ggj.java.hystrix.config;

import static java.lang.Thread.sleep;

import com.netflix.hystrix.*;

import lombok.extern.slf4j.Slf4j;


/**
 * https://github.com/Netflix/Hystrix/wiki/Configuration
 *
 * @author:gaoguangjin
 * @date 2017/5/23 16:26
 */
@Slf4j
public class MainConfig {

    public static void main(String[] args) {
        //默认超时时间是1000ms
        timeOutDefualtconfig();
        //可以将CommandConfig的run()方法的sleep时间打开测试
//        timeOutconfig();
        //策略模式
//        testSemaphore();

//        fallback();
        //没有重写fallback
//        withoutFallBack();
        //闸路
//        circuitBreaker();
        //
//        pool();
    }

    private static void withoutFallBack() {
        try {
            String result = new WithOutFallbackCommandConfig(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("WithOutFallbackCommandConfig"))).execute();
            log.info("withoutFallBack:{}", result);
        } catch (Exception e) {
            log.error("withoutFallBack:{}", e.getLocalizedMessage());
        }
    }

    private static void pool() {
        CommandConfig.Setter setter = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CommandConfig-config"));
        setter.andCommandKey(HystrixCommandKey.Factory.asKey("CommandConfig-method"));
        //在调用方配置，被该调用方的所有方法的超时时间都是该值
        HystrixCommandProperties.Setter defaultSetter = HystrixCommandProperties.Setter();
        HystrixThreadPoolProperties.Setter pooldefaultSetter = HystrixThreadPoolProperties.Setter()
                //
                .withCoreSize(20)
                .withMaximumSize(10)
                //默认-1，使用SynchronousQueue。其他值则使用 LinkedBlockingQueue。如果要从-1换成其他值则需重启，即该值不能动态调整，若要动态调整，需要使用到下边这个配置
                .withMaxQueueSize(-1)
                //默认的是5  如果maxQueueSize==-1 则不起作用
                .withQueueSizeRejectionThreshold(5);
        setter.andCommandPropertiesDefaults(defaultSetter);

    }

    //requestVolumeThreshold 最小请求数，用来跳闸回路 默认20
    private static void circuitBreaker() {
        //设置组别和命令的key
        CommandConfig.Setter setter = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CommandConfig-config"));
        setter.andCommandKey(HystrixCommandKey.Factory.asKey("CommandConfig-method"));
        //在调用方配置，被该调用方的所有方法的超时时间都是该值
        HystrixCommandProperties.Setter defaultSetter = HystrixCommandProperties.Setter()
                .withCircuitBreakerEnabled(true)
                //设置一个滑动窗口内触发熔断的最少请求量，默认20。
                .withCircuitBreakerRequestVolumeThreshold(10)
                //设置触发熔断后，拒绝请求后多长时间开始尝试再次执行。默认5000ms。
                .withCircuitBreakerSleepWindowInMilliseconds(500)
                // 置为true时，所有请求都将被拒绝，直接到fallback
//                .withCircuitBreakerForceOpen(true)
                //触发熔断的错误比例,默认50%
//                .withCircuitBreakerErrorThresholdPercentage(50)
                ;
        setter.andCommandPropertiesDefaults(defaultSetter);

        for (int i = 0; i < 22; i++) {
            try {
                // circuit-break can be worked at 1st time, but after, it doesn't be worked 如果触发fallback的机制
                //不是超时而是抛出异常最好休眠才能看到效果
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new CircuitBreakerCommandConfig(setter, i).execute();
        }
    }

    //Fallback的一些配置适用于ExecutionIsolationStrategy.SEMAPHORE和ExecutionIsolationStrategy.THREAD
    //withFallbackIsolationSemaphoreMaxConcurrentRequests 并发的时候如果同时调用FallBack次数超过设置的值就不会继续调用了
    private static void fallback() {
        //设置组别和命令的key
        CommandConfig.Setter setter = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CommandConfig-config"));
        setter.andCommandKey(HystrixCommandKey.Factory.asKey("CommandConfig-method"));
        //在调用方配置，被该调用方的所有方法的超时时间都是该值
        HystrixCommandProperties.Setter defaultSetter = HystrixCommandProperties.Setter()
                //.withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                //错误的时候一般会调用fallback，但当错误次数达到一定次数直接拦截不会再调用fallback
                // .withFallbackIsolationSemaphoreMaxConcurrentRequests(11);
                .withFallbackIsolationSemaphoreMaxConcurrentRequests(1);
        setter.andCommandPropertiesDefaults(defaultSetter);
        for (int i = 0; i < 15; i++) {
            new Thread(() -> {
                try {
                    log.info("返回结果：{}", new FallbackCommandConfig(setter).execute());
                } catch (Exception e) {
//                    log.error(""+e.getLocalizedMessage());
                }
            }).start();
        }
    }

    //（1）线程池隔离模式：使用一个线程池来存储当前的请求，线程池对请求作处理，设置任务返回处理超时时间，堆积的请求堆积入线程池队列。
    // 这种方式需要为每个依赖的服务申请线程池，有一定的资源消耗，好处是可以应对突发流量（流量洪峰来临时，处理不完可将数据存储到线程池队里慢慢处理）
    // （2）信号量隔离模式：使用一个原子计数器（或信号量）来记录当前有多少个线程在运行，请求来先判断计数器的数值，若超过设置的最大线程个数则丢弃改类型的新请求，
    // 若不超过则执行计数操作请求来计数器+1，请求返回计数器-1。
    // 这种方式是严格的控制线程且立即返回模式，无法应对突发流量（流量洪峰来临时，处理的线程超过数量，其他的请求会直接返回，不继续去请求依赖的服务）
    private static void testSemaphore() {
        //设置组别和命令的key
        CommandConfig.Setter setter = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CommandConfig-config"));
        setter.andCommandKey(HystrixCommandKey.Factory.asKey("CommandConfig-method"));
        //在调用方配置，被该调用方的所有方法的超时时间都是该值
        HystrixCommandProperties.Setter defaultSetter = HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(1000 * 20)
                // //默认是THREAD，每个服务单独分开定义限制的请求数.SEMAPHORE 请求数号量计数（整体的一个量）,如果超过默认的10 就丢弃
//                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                //当策略模式为SEMAPHORE 才有效，默认是10
                .withExecutionIsolationSemaphoreMaxConcurrentRequests(2);
        setter.andCommandPropertiesDefaults(defaultSetter);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                log.info("返回结果：{}", new CommandConfig(setter).execute());
            }).start();
        }
    }

    private static void timeOutDefualtconfig() {
        String result = new CommandConfig(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CommandConfig"))).execute();
        log.info(result);
    }

    private static void timeOutconfig() {
        //设置组别和命令的key
        CommandConfig.Setter setter = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CommandConfig-config"));
        setter.andCommandKey(HystrixCommandKey.Factory.asKey("CommandConfig-method"));
        //在调用方配置，被该调用方的所有方法的超时时间都是该值
        HystrixCommandProperties.Setter defaultSetter = HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(1000 * 20);
        setter.andCommandPropertiesDefaults(defaultSetter);
        CommandConfig command = new CommandConfig(setter);
        String result = command.execute();
        log.info(result);
    }
}
