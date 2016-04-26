package com.ggj.httpclient.someidea;

import com.alibaba.fastjson.JSONObject;
import com.ggj.httpclient.someidea.redis.RedisDaoTemplate;
import com.ggj.httpclient.someidea.redis.callback.RedisCallback;
import com.ggj.httpclient.someidea.redis.callback.TransactionCallBack;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author:gaoguangjin
 * @Description:将redis里面html信息解析出来
 * @Email:335424093@qq.com
 * @Date 2016/4/11 10:56
 */
@Slf4j
public class FormatSuccessAddress {
	private static final String REDIS_KEY_ADDRESSID_SUCCESS = "ule_address_success";
	private static final String REDIS_KEY_ADDRESSID_SUCCESS_LIST = "ule_address_success_list";
	private static final String REDIS_KEY_ADDRESSINFO_LIST = "ule_address_info_list";
	private static int threadNumber;//144
	private static Long page = 100L;//
	
	public static void main(String[] args) {
		format();
	}
	
	private static void format() {
		long beingTime = System.currentTimeMillis();
		// 初始化线程大小
		initThreadNumer();
		ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);
		for (int i = 0; i < threadNumber; i++) {
			executorService.execute(executeThread(i));
		}
		executorService.shutdown();
		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			long endTime = System.currentTimeMillis();
			log.info("执行解析html时间：" + (endTime - beingTime));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	private static Runnable executeThread(int pageSize) {
		final long beginId = pageSize * page;
		final long endId = beginId + page;
		final List<AddressInfo> addressInfoList = new LinkedList<AddressInfo>();
		return new Thread(() -> {
			List<String> listId = RedisDaoTemplate.getRedisDaoTemplate().execute(new RedisCallback<List<String>>() {
				@Override
				public List<String> doInRedis(Jedis jedis) {
					return jedis.lrange(REDIS_KEY_ADDRESSID_SUCCESS_LIST, beginId, endId);
				}
			});
            //并行因为线程数的原因不一定很快2.30分钟
//             userThread(listId,addressInfoList);
            //2分钟~
            notUserThread(listId,addressInfoList);
		});
	}

    private static void notUserThread(List<String> listId, List<AddressInfo> addressInfoList) {
        for (String id : listId) {
            praseHtmlById(id, addressInfoList);
        }
        saveListAddressInfoToRedis(addressInfoList);
    }

    /**
     * 开启子线程解析
     * @param listId
     * @param addressInfoList
     */
    private static void userThread(List<String> listId, List<AddressInfo> addressInfoList) {
        int page=50;
        int size=listId.size()/page;

        CyclicBarrier cy=new CyclicBarrier(size,new SaveAddressInoTask(addressInfoList));

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i <size ; i++) {
            int begin=i*page;
            int end=begin+page;
            if(i==size)
                end=listId.size();
            executorService.execute(new praseHtmlByIdThread(cy,addressInfoList,listId.subList(begin,end)));
        }
        executorService.shutdown();
    }


    private static class praseHtmlByIdThread implements Runnable {
        CyclicBarrier cy;
        List<String> ids;
        List<AddressInfo> addressInfoList;
        public praseHtmlByIdThread(CyclicBarrier cy, List<AddressInfo> addressInfoList,  List<String> ids) {
            this.cy=cy;
            this.addressInfoList=addressInfoList;
            this.ids=ids;
        }

        @Override
        public void run() {
            for (String id : ids) {
                praseHtmlById(id, addressInfoList);
            }
            try {
                cy.await();
            } catch (InterruptedException|BrokenBarrierException e) {
                log.error("CyclicBarriery 异常"+e.getLocalizedMessage());
            }
        }
    }

    /**
     * 到达栅栏点后再保持到redis
     */
    private static class SaveAddressInoTask implements Runnable{
        List<AddressInfo> addressInfoList;

        public SaveAddressInoTask(List<AddressInfo> addressInfoList) {
            this.addressInfoList=addressInfoList;
        }
        @Override
        public void run() {
            log.info("SaveAddressInoTask");
            saveListAddressInfoToRedis(addressInfoList);
        }
    }

    /**
	 * 序列化地址信息保存到redis
	 * @param addressInfoList
	 */
	private static void saveListAddressInfoToRedis(final List<AddressInfo> addressInfoList) {
		RedisDaoTemplate.getRedisDaoTemplate().execute(new TransactionCallBack() {
			@Override
			public void execute(Transaction transation) {
				for (AddressInfo addressInfo : addressInfoList) {
					transation.lpush(REDIS_KEY_ADDRESSINFO_LIST, JSONObject.toJSON(addressInfo).toString());
				}
			}
		});
	}
	
	/**
	 * 根据id去REDIS_KEY_ADDRESSID_SUCCESS  map里面去查询对应的html
	 * @param key
	 * @param addressInfoList
	 */
	private static void praseHtmlById(final String key, List<AddressInfo> addressInfoList) {
		AddressInfo addressInfo = new AddressInfo();
		String html = RedisDaoTemplate.getRedisDaoTemplate().execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(Jedis jedis) {
				return jedis.hget(REDIS_KEY_ADDRESSID_SUCCESS, key);
			}
		});
		praseHtml(html, addressInfo);
		addressInfoList.add(addressInfo);
	}
	
	/**
	 * 分割html，截取需要的值
	 *
	 * @param html
	 * @param addressInfo
	 */
	private static void praseHtml(String html, AddressInfo addressInfo) {
		try {
			String name = html.split("maxlength=\"300\" value=\"")[1].split("\"/>")[0];
			String phone = html.split("maxlength=\"11\" value=\"")[1].split("\"/>")[0];
			String userAddress = html.split("name=\"userAddress\" value=\"")[1].split("\"/>")[0];
			addressInfo.setName(name);
			addressInfo.setPhone(phone);
			addressInfo.setUserAddress(userAddress);
		} catch (Exception e) {
			log.info("解析HTML出错" + e.getLocalizedMessage());
		}
	}
	
	private static void initThreadNumer() {
		Long size = RedisDaoTemplate.getRedisDaoTemplate().execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(Jedis jedis) {
				return jedis.llen(REDIS_KEY_ADDRESSID_SUCCESS_LIST);
			}
		});
		threadNumber = (int) (size / page);
		log.info("开启threadNumber=" + threadNumber + "个线程");
	}


}
