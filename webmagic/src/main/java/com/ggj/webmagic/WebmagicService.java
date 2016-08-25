package com.ggj.webmagic;


import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSONObject;
import com.ggj.webmagic.autoconfiguration.TieBaImageIdMessageListener;
import com.ggj.webmagic.tieba.ContentIdProcessor;
import com.ggj.webmagic.tieba.ContentImageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ggj.webmagic.autoconfiguration.TieBaConfiguration;
import com.ggj.webmagic.tieba.TopBean;
import com.ggj.webmagic.tieba.TopProcessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;


/**
 * @author:gaoguangjin
 * @date 2016/8/19 11:17
 */
@Service
@Slf4j
public class WebmagicService {
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private TieBaConfiguration tieBaConfiguration;
	
	@Autowired
	private TopProcessor topProcessor;
	@Autowired
	private ContentIdProcessor contentIdProcessor;

	/**
	 * 查询是否缓存指定tieba名称
	 *
	 * @param tieBaName
	 * @return
	 */
	public boolean isCacheTieBa(String tieBaName) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
				return redisConnection.exists(getByte(tieBaName));
			}
		});
	}
	
	public void getTieBaTop(String tieBaName, Integer size, Model model) {
        List<String> xAxis=new ArrayList<>();
        List<String> series=new ArrayList<>();
		Set<byte[]>  set=getTieBaTopFromRedis(tieBaName,size);
		for(Iterator<byte[]> iterator = set.iterator(); iterator.hasNext();) {
			TopBean topBeans = JSONObject.parseObject(getString(iterator.next()), TopBean.class);
			xAxis.add(topBeans.getUserName());
			series.add(topBeans.getExperience());
		}
		model.addAttribute("xAxis", xAxis);
		model.addAttribute("series", series);
	}
	/**
	 * 根据等级分组
	 * @param tieBaName
	 * @param model
	 */
	public void getLevelCount(String tieBaName, Model model) {
		List<Integer> xAxis=new ArrayList<>();
		List<Integer> series=new ArrayList<>();
		Map<Integer, Integer> map = new HashMap<Integer,Integer>();
		Set<byte[]>  set=getTieBaTopFromRedis(tieBaName,null);
		for(Iterator<byte[]> iterator = set.iterator(); iterator.hasNext();) {
			TopBean topBeans = JSONObject.parseObject(getString(iterator.next()), TopBean.class);
			Integer level=Integer.parseInt(topBeans.getLevel());
			Integer count=map.get(level);
			map.put(level,count==null?1:count+1);
		}
		for(Iterator<Integer> iter = map.keySet().iterator(); iter.hasNext();) {
			Integer level = iter.next();
			Integer count = map.get(level);
			xAxis.add(level);
			series.add(count);
		}
		model.addAttribute("xAxis", xAxis);
		model.addAttribute("series", series);
	}

	private Set<byte[]> getTieBaTopFromRedis(String tieBaName, Integer size) {
		return  redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
			public Set<byte[]> doInRedis(RedisConnection redisConnection) throws DataAccessException {
				byte[] key = getByte(tieBaName);
				if (redisConnection.exists(key)) {
					return redisConnection.zRange(key, 0, size==null?-1:size);
				}
				return null;
			}
		});
	}

	/**
     * 根据贴吧名称调用tiebaTop爬虫
     * @throws UnsupportedEncodingException
     */
	public void addTieBaTop() throws UnsupportedEncodingException {
        Map<String, TopBean> map=null;
        //贴吧名称存放到redis
        Set<byte[]> setTieBaName = getCacheTieBaName();
        for (byte[] bytes : setTieBaName) {
            String name = getString(bytes);
            putResultToRedis(name,topProcessor.start(name));
        }
    }
	private void putResultToRedis(String name, ConcurrentHashMap<String, TopBean> map) {
        redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] key = getByte(name);
                redisConnection.del(key);
                for(Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) {
                    String id = iter.next();
                    TopBean topBean = map.get(id);
                    redisConnection.zAdd(key,Double.parseDouble(topBean.getIndex()), getByte(JSONObject.toJSON(topBean).toString()));
                }
                return null;
            }
        });
    }
	public static  byte[] getByte(String str) {
		try {
			return str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error("getByte error:"+e.getLocalizedMessage());
		}
		return null;
	}

	public static String getString(byte[] bytes) {
		try {
			return new String(bytes, "utf-8");
		} catch (UnsupportedEncodingException e) {
            log.error("getString error:"+e.getLocalizedMessage());
		}
		return null;
	}

	public void addTieBaImage() {
		Set<byte[]> setTieBaName =getCacheTieBaName();
		for (byte[] bytes : setTieBaName) {
			String name = getString(bytes);
			contentIdProcessor.start(name);
		}
	}

	private Set<byte[]> getCacheTieBaName() {
		//贴吧名称存放到redis
		String[] tiebaName = tieBaConfiguration.getTiebaName();
		return redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
			public Set<byte[]> doInRedis(RedisConnection redisConnection) throws DataAccessException {
				byte[] key = getByte("tieba_names");
				if (!redisConnection.exists(key)) {
					for (String name : tiebaName)
						redisConnection.sAdd(key, getByte(name));
				}
				return redisConnection.sMembers(key);
			}
		});
	}

	public void getTieBaImage(Model model) {
		redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
				byte[] key = getByte(TieBaImageIdMessageListener.TIEBA_CONTENT_REDIS_KEY);
               Map<String,List<String>> map=new HashMap<String, List<String>>();
				Map<byte[], byte[]> mapByte = redisConnection.hGetAll(key);
				for(Iterator<byte[]> iter = mapByte.keySet().iterator(); iter.hasNext();) {
					byte[] pageUrl=iter.next();
					byte[] imageUrl = mapByte.get(pageUrl);
					if(imageUrl!=null) {
						List listImage = JSONObject.parseObject(getString(imageUrl), ArrayList.class);
						map.put(getString(pageUrl), listImage);
					}
				}
				model.addAttribute("data",map);
				return null;
			}
		});

	}
}
