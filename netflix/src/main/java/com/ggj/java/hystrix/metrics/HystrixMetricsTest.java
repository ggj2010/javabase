package com.ggj.java.hystrix.metrics;

import com.ggj.java.hystrix.config.CommandConfig;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.metric.consumer.HystrixDashboardStream;
import com.netflix.hystrix.serial.SerialHystrixDashboardData;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * HystrixMetricsStreamServlet
 *
 * @author:gaoguangjin
 * @date 2017/5/25 10:07
 */
@Slf4j
public class HystrixMetricsTest {
    public static void main(String[] args) {


        CommandConfig.Setter setter = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HystrixMetrics-config"));
        setter.andCommandKey(HystrixCommandKey.Factory.asKey("HystrixMetrics-method"));
        //在调用方配置，被该调用方的所有方法的超时时间都是该值
        HystrixCommandProperties.Setter defaultSetter = HystrixCommandProperties.Setter();
        setter.andCommandPropertiesDefaults(defaultSetter);
        Observable<HystrixDashboardStream.DashboardData> dashboardDataOservable = HystrixDashboardStream.getInstance().observe();
        Observable<String> sampleStream = dashboardDataOservable.concatMap(new Func1<HystrixDashboardStream.DashboardData, Observable<String>>() {
            public Observable<String> call(HystrixDashboardStream.DashboardData dashboardData) {
                return Observable.from(SerialHystrixDashboardData.toMultipleJsonStrings(dashboardData));
            }
        });

        Subscription sampleSubscription = sampleStream.observeOn(Schedulers.io()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(String sampleDataAsString) {
                log.info("sampleDataAsString=" + sampleDataAsString);
            }
        });
        if (sampleSubscription.isUnsubscribed()) {
            sampleSubscription.unsubscribe();
        }

        for (; ; ) {
            new MetricsCommand(setter).execute();
        }
    }
}
