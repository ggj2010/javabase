package com.ggj.java.rxjava;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * http://www.jianshu.com/p/5e93c9101dc5
 * @author:gaoguangjin
 * @date 2017/5/23 14:43
 */
@Slf4j
public class QucikStart {
	
	//
	// Observable：发射源，英文释义“可观察的”，在观察者模式中称为“被观察者”或“可观察对象”；
	//
	// Observer：接收源，英文释义“观察者”，没错！就是观察者模式中的“观察者”，可接收Observable、Subject发射的数据；
	//
	// Subject：Subject是一个比较特殊的对象，既可充当发射源，也可充当接收源，为避免初学者被混淆，本章将不对Subject做过多的解释和使用，重点放在Observable和Observer上，先把最基本方法的使用学会，后面再学其他的都不是什么问题；
	//
	// Subscriber：“订阅者”，也是接收源，那它跟Observer有什么区别呢？Subscriber实现了Observer接口，比Observer多了一个最重要的方法unsubscribe(
	// )，用来取消订阅，当你不再想接收数据了，可以调用unsubscribe( )方法停止接收，Observer 在 subscribe() 过程中,最终也会被转换成 Subscriber
	// 对象，一般情况下，建议使用Subscriber作为接收源；
	//
	// Subscription：Observable调用subscribe( )方法返回的对象，同样有unsubscribe( )方法，可以用来取消订阅事件；
	//
	// Action0：RxJava中的一个接口，它只有一个无参call（）方法，且无返回值，同样还有Action1，Action2...Action9等，Action1封装了含有 1 个参的call（）方法，即call（T
	// t），Action2封装了含有 2 个参数的call方法，即call（T1 t1，T2 t2），以此类推；
	//
	// Func0：与Action0非常相似，也有call（）方法，但是它是有返回值的，同样也有Func0、Func1...Func9;
	public static void main(String[] args) {
		Observable<String> sender = Observable.create(new Observable.OnSubscribe<String>() {
			
			@Override
			public void call(Subscriber<? super String> subscriber) {
				subscriber.onNext("Hi，Weavey！"); // 发送数据"Hi，Weavey！"
				// 需要自己手动调用
				subscriber.onCompleted();
				// subscriber.onError(new Exception("ddd"));
			}
		});
		Observer<String> receiver = new Observer<String>() {
			@Override
			public void onCompleted() {
				// 数据接收完成时调用
				log.info("onCompleted");
			}
			@Override
			public void onError(Throwable e) {
				// 发生错误调用
				log.info("onError");
			}
			@Override
			public void onNext(String s) {
				// 正常接收数据调用
				log.info(s); // 将接收到来自sender的问候"Hi，Weavey！"
			}
		};
		// 射源和接收源关联起来
		 sender.subscribe(receiver);


		// 使用just( )，将为你创建一个Observable并自动为你调用onNext( )发射数据：
		Observable<String> justObservable = Observable.just("just1", "just2");
		// justObservable.subscribe(receiver);
		// 注意，just()方法也可以传list，但是发送的是整个list对象，而from（）发送的是list的一个item
		List<String> list = new ArrayList<>();
		list.add("from1");
		list.add("from2");
		list.add("from3");
		Observable<String> fromObservable = Observable.from(list); // 遍历list 每次发送一个
		// fromObservable.subscribe(receiver);
		// concatMap
		Observable concatMapObservale = Observable
				.just("http://www.baidu.com/", "http://www.google.com/", "https://www.bing.com/")
				.concatMap( (new Func1<String, Observable<String>>() {
					
					@Override
					public Observable<String> call(String s) {
						return createIpObservable(s);
					}
				}));
		concatMapObservale.subscribe(receiver);
	}
	
	private static Observable<String> createIpObservable(String s) {
		return Observable.create(new Observable.OnSubscribe<String>() {
			
			@Override
			public void call(Subscriber<? super String> subscriber) {
				subscriber.onNext(s);
				subscriber.onCompleted();
			}
		});
	}
}
