package com.ggj.java.hystrix.metrics;

import com.netflix.hystrix.HystrixCommand;

/**
 * @author:gaoguangjin
 * @date 2017/5/25 10:08
 */
public class MetricsCommand extends HystrixCommand<String> {
	
	protected MetricsCommand(Setter setter) {
		super(setter);
	}
	
	@Override
	protected String run() throws Exception {
        Thread.sleep(700);
		//休眠700 打印时间： "latencyTotal_mean": 703,
		/**
		 * {
		 "type": "HystrixCommand",
		 "name": "HystrixMetrics-method",
		 "group": "HystrixMetrics-config",
		 "currentTime": 1495694702433,
		 "isCircuitBreakerOpen": false,
		 "errorPercentage": 0,
		 "errorCount": 0,
		 "requestCount": 15,
		 "rollingCountBadRequests": 0,
		 "rollingCountCollapsedRequests": 0,
		 "rollingCountEmit": 0,
		 "rollingCountExceptionsThrown": 0,
		 "rollingCountFailure": 0,
		 "rollingCountFallbackEmit": 0,
		 "rollingCountFallbackFailure": 0,
		 "rollingCountFallbackMissing": 0,
		 "rollingCountFallbackRejection": 0,
		 "rollingCountFallbackSuccess": 0,
		 "rollingCountResponsesFromCache": 0,
		 "rollingCountSemaphoreRejected": 0,
		 "rollingCountShortCircuited": 0,
		 "rollingCountSuccess": 14,
		 "rollingCountThreadPoolRejected": 0,
		 "rollingCountTimeout": 0,
		 "currentConcurrentExecutionCount": 1,
		 "rollingMaxConcurrentExecutionCount": 1,
		 "latencyExecute_mean": 703,
		 "latencyExecute": {
		 "0": 700,
		 "25": 701,
		 "50": 701,
		 "75": 703,
		 "90": 705,
		 "95": 705,
		 "99": 724,
		 "100": 724,
		 "99.5": 724
		 },
		 "latencyTotal_mean": 703,
		 "latencyTotal": {
		 "0": 701,
		 "25": 701,
		 "50": 702,
		 "75": 703,
		 "90": 706,
		 "95": 706,
		 "99": 727,
		 "100": 727,
		 "99.5": 727
		 },
		 "propertyValue_circuitBreakerRequestVolumeThreshold": 20,
		 "propertyValue_circuitBreakerSleepWindowInMilliseconds": 5000,
		 "propertyValue_circuitBreakerErrorThresholdPercentage": 50,
		 "propertyValue_circuitBreakerForceOpen": false,
		 "propertyValue_circuitBreakerForceClosed": false,
		 "propertyValue_circuitBreakerEnabled": true,
		 "propertyValue_executionIsolationStrategy": "THREAD",
		 "propertyValue_executionIsolationThreadTimeoutInMilliseconds": 1000,
		 "propertyValue_executionTimeoutInMilliseconds": 1000,
		 "propertyValue_executionIsolationThreadInterruptOnTimeout": true,
		 "propertyValue_executionIsolationThreadPoolKeyOverride": null,
		 "propertyValue_executionIsolationSemaphoreMaxConcurrentRequests": 10,
		 "propertyValue_fallbackIsolationSemaphoreMaxConcurrentRequests": 10,
		 "propertyValue_metricsRollingStatisticalWindowInMilliseconds": 10000,
		 "propertyValue_requestCacheEnabled": true,
		 "propertyValue_requestLogEnabled": true,
		 "reportingHosts": 1,
		 "threadPool": "HystrixMetrics-config"
		 }
		 */



//        Thread.sleep(500);
		//打印结果 latencyTotal_mean：520
		/**
		 *
		 * {
		 "type": "HystrixCommand",
		 "name": "HystrixMetrics-method",
		 "group": "HystrixMetrics-config",
		 "currentTime": 1495694360640,
		 "isCircuitBreakerOpen": false,
		 "errorPercentage": 0,
		 "errorCount": 0,
		 "requestCount": 20,
		 "rollingCountBadRequests": 0,
		 "rollingCountCollapsedRequests": 0,
		 "rollingCountEmit": 0,
		 "rollingCountExceptionsThrown": 0,
		 "rollingCountFailure": 0,
		 "rollingCountFallbackEmit": 0,
		 "rollingCountFallbackFailure": 0,
		 "rollingCountFallbackMissing": 0,
		 "rollingCountFallbackRejection": 0,
		 "rollingCountFallbackSuccess": 0,
		 "rollingCountResponsesFromCache": 0,
		 "rollingCountSemaphoreRejected": 0,
		 "rollingCountShortCircuited": 0,
		 "rollingCountSuccess": 20,
		 "rollingCountThreadPoolRejected": 0,
		 "rollingCountTimeout": 0,
		 "currentConcurrentExecutionCount": 1,
		 "rollingMaxConcurrentExecutionCount": 1,
		 "latencyExecute_mean": 516,
		 "latencyExecute": {
		 "0": 500,
		 "25": 500,
		 "50": 501,
		 "75": 502,
		 "90": 503,
		 "95": 503,
		 "99": 813,
		 "100": 813,
		 "99.5": 813
		 },
		 "latencyTotal_mean": 520,
		 "latencyTotal": {
		 "0": 500,
		 "25": 500,
		 "50": 501,
		 "75": 502,
		 "90": 503,
		 "95": 504,
		 "99": 887,
		 "100": 887,
		 "99.5": 887
		 },
		 "propertyValue_circuitBreakerRequestVolumeThreshold": 20,
		 "propertyValue_circuitBreakerSleepWindowInMilliseconds": 5000,
		 "propertyValue_circuitBreakerErrorThresholdPercentage": 50,
		 "propertyValue_circuitBreakerForceOpen": false,
		 "propertyValue_circuitBreakerForceClosed": false,
		 "propertyValue_circuitBreakerEnabled": true,
		 "propertyValue_executionIsolationStrategy": "THREAD",
		 "propertyValue_executionIsolationThreadTimeoutInMilliseconds": 1000,
		 "propertyValue_executionTimeoutInMilliseconds": 1000,
		 "propertyValue_executionIsolationThreadInterruptOnTimeout": true,
		 "propertyValue_executionIsolationThreadPoolKeyOverride": null,
		 "propertyValue_executionIsolationSemaphoreMaxConcurrentRequests": 10,
		 "propertyValue_fallbackIsolationSemaphoreMaxConcurrentRequests": 10,
		 "propertyValue_metricsRollingStatisticalWindowInMilliseconds": 10000,
		 "propertyValue_requestCacheEnabled": true,
		 "propertyValue_requestLogEnabled": true,
		 "reportingHosts": 1,
		 "threadPool": "HystrixMetrics-config"
		 }
		 */

		return "result";
	}
	
	@Override
	protected String getFallback() {
		return "fallBack";
	}
}
