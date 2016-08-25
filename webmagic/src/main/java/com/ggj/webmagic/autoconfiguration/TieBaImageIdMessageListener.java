package com.ggj.webmagic.autoconfiguration;

import static com.ggj.webmagic.WebmagicService.getByte;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ggj.webmagic.WebmagicService;
import com.ggj.webmagic.tieba.ContentImageProcessor;

import lombok.extern.slf4j.Slf4j;

/**
 * redis pub/sub 订阅者
 * @author:gaoguangjin
 * @date 2016/8/24 18:06
 */
@Slf4j
@Service
public class TieBaImageIdMessageListener implements MessageListener {
    public static final   String TIEBA_CONTENT_REDIS_KEY="tieba_content_image";
	@Autowired
	private ContentImageProcessor contentImageProcessor;
	@Autowired
	private TieBaConfiguration tieBaConfiguration;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public void onMessage(Message message, byte[] bytes) {
		String jsonStr = WebmagicService.getString(message.getBody());
		List<String> list = JSONObject.parseObject(jsonStr, ArrayList.class);
		log.info("待同步pageID数量："+list.size());
		list=removeExistId(list);
		log.info("去除已同步pageID后数量："+list.size());
        ConcurrentHashMap<byte[], byte[]> map = contentImageProcessor.start(list);
		redisTemplate.executePipelined(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
				byte[] key = getByte(TIEBA_CONTENT_REDIS_KEY);
//					redisConnection.del(key);
					if(map.size()>0)
                    redisConnection.hMSet(key,map);
				return null;
			}
		});
	}

	/**
	 * 优化 去除已经爬过的id
	 * @param list
	 * @return
     */
	private List<String> removeExistId(List<String> list) {
		String prefixUrl=tieBaConfiguration.getTiebaContentPageUrl();
		return redisTemplate.execute(new RedisCallback<List<String>>() {
			@Override
			public List<String> doInRedis(RedisConnection redisConnection) throws DataAccessException {
				List<String> listId=new ArrayList<>();
				byte[] key = getByte(TIEBA_CONTENT_REDIS_KEY);
				for (String id : list) {
					byte[] setKey=getByte(prefixUrl+id);
					if(!redisConnection.hExists(key, setKey))
						listId.add(id);
				}
				return listId;
			}
		});

	}
}
