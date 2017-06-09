package com.ggj.java.hystrix.quickstart;


import java.util.concurrent.Future;

import com.netflix.hystrix.HystrixEventType;
import com.netflix.hystrix.HystrixInvokableInfo;
import com.netflix.hystrix.HystrixRequestLog;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;


/**
 * @author:gaoguangjin
 * @date 2017/5/23 13:55
 */
@Slf4j
public class QuickStart {
    public static void main(String[] args) {
        //正常测试
//        commandHelloWorldTest();
        //测试fallback
//        commandHelloWorldFailureTest();
        //快速失败，去除fallback方法
//        commandThatFailsFastTest();
        //失败的时候 fallback 容错返回
//       commandThatFailsSilentlyTest();

        //容错切换服务
        commandWithFallbackViaNetworkTest();
    }

    private static void commandWithFallbackViaNetworkTest() {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
             log.info(new CommandWithFallbackViaNetwork(1).execute());

            HystrixInvokableInfo<?> command1 = HystrixRequestLog.getCurrentRequest().getAllExecutedCommands().toArray(new HystrixInvokableInfo<?>[2])[0];
            log.info(command1.getCommandKey().name());
            log.info(""+command1.getExecutionEvents().contains(HystrixEventType.FAILURE));

            HystrixInvokableInfo<?> command2 = HystrixRequestLog.getCurrentRequest().getAllExecutedCommands().toArray(new HystrixInvokableInfo<?>[2])[1];
            log.info(command2.getCommandKey().name());
            log.info(""+command2.getExecutionEvents().contains(HystrixEventType.FAILURE));
        } finally {
            context.shutdown();
        }
    }

    /**
     * 容错的返回相同数据类型
     */
    private static void commandThatFailsSilentlyTest() {

        log.info(new CommandThatFailsSilently(true).execute().toString());
    }

    private static void commandThatFailsFastTest() {
        try {
            new CommandThatFailsFast(true).execute();
        }catch (Exception e){
            log.error("commandThatFailsFastTest:{}",e.getLocalizedMessage());
        }
    }

    private static void commandHelloWorldTest() {
        try {
//            testSynchronous();
//            testAsynchronous1();
//            testAsynchronous2();
            testObservable();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

    private static void commandHelloWorldFailureTest() {
        try {
            testSynchronousError();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

    private static void testSynchronousError() {
        log.info(new CommandHelloFailure("World").execute());
    }

    /**
     * 同步
     */
    public static void testSynchronous() {
        log.info(new CommandHelloWorld("World").execute());
        log.info(new CommandHelloWorld("Bob").execute());
    }

    /**
     * 异步
     */
    public static void testAsynchronous1() throws Exception {
        log.info(new CommandHelloWorld("World").queue().get());
        log.info(new CommandHelloWorld("Bob").queue().get());
    }

    public static void testAsynchronous2() throws Exception {
        Future<String> fWorld = new CommandHelloWorld("World").queue();
        Future<String> fBob = new CommandHelloWorld("Bob").queue();
        log.info(fWorld.get());
        log.info(fBob.get());
    }

    public static void testObservable() throws Exception {
        Observable<String> fWorld = new CommandHelloWorld("World").observe();
        Observable<String> fBob = new CommandHelloWorld("Bob").observe();
        // blocking
        fWorld.toBlocking().single();
        // fBob.toBlocking().single();
        // non-blocking
        // - this is a verbose anonymous inner-class approach and doesn't do assertions
        fWorld.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                log.info("onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                log.info("onError: " + e);
            }

            @Override
            public void onNext(String v) {
                log.info("onNext: " + v);
            }
        });
        fBob.subscribe(new Action1<String>() {
            @Override
            public void call(String v) {
                log.info("onNext: " + v);
            }
        });
    }
}
