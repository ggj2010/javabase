/**
 * Copyright 2012 Netflix, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ggj.java.hystrix.quickstart;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;

/**
 * Sample {@link HystrixCommand} that implements fallback logic that requires
 * network traffic and thus executes another {@link HystrixCommand} from the {@link #getFallback()} method.
 * <p>
 * Note also that the fallback command uses a separate thread-pool as well even though
 * it's in the same command group.
 * <p>
 * It needs to be on a separate thread-pool otherwise the first command could saturate it
 * and the fallback command never have a chance to execute.
 * 当一个服务不可以用的时候，快速切换到另外一个服务
 * 比如有两个短信服务商，当A服务商挂了的时候，切换到B服务商
 */
public class CommandWithFallbackViaNetwork extends HystrixCommand<String> {
	
	private final int id;
	
	protected CommandWithFallbackViaNetwork(int id) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceX"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("GetValueCommand")));
		this.id = id;
	}
	
	@Override
	protected String run() {
		// RemoteServiceXClient.getValue(id);
		throw new RuntimeException("force failure for example");
	}
	
	@Override
	protected String getFallback() {
		return new FallbackViaNetwork(id).execute();
	}
	
	private static class FallbackViaNetwork extends HystrixCommand<String> {
		
		private final int id;
		
		public FallbackViaNetwork(int id) {
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceX"))
					.andCommandKey(HystrixCommandKey.Factory.asKey("GetValueFallbackCommand"))
					// use a different threadpool for the fallback command
					// so saturating the RemoteServiceX pool won't prevent
					// fallbacks from executing
					.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("RemoteServiceXFallback")));
			this.id = id;
		}
		
		@Override
		protected String run() {
			// MemCacheClient.getValue(id);
			throw new RuntimeException("the fallback also failed");
		}
		
		@Override
		protected String getFallback() {
			// the fallback also failed
			// so this fallback-of-a-fallback will
			// fail silently and return null
			return null;
		}
	}
}
