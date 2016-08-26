package com.ggj.webmagic.autoconfiguration;

import static com.ggj.webmagic.WebmagicService.getByte;
import static com.ggj.webmagic.autoconfiguration.TieBaImageIdMessageListener.TIEBA_CONTENT_IMAGE_KEY;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSONObject;
import com.ggj.webmagic.WebmagicService;
import com.ggj.webmagic.tieba.bean.ContentBean;
import org.springframework.stereotype.Component;

/**
 * @author:gaoguangjin
 * @date 2016/8/27 2:16
 */
@Component
public class TieBaNoImageIdMessageListener implements MessageListener {
	
	public static final String TIEBA_CONTENT_NO_IMAGE_KEY = "tieba_content_no_image_";
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Override
	public void onMessage(Message message, byte[] bytes) {
		String jsonStr = WebmagicService.getString(message.getBody());
		ContentBean contentBean = JSONObject.parseObject(jsonStr, ContentBean.class);
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
				byte[] key = getByte(TIEBA_CONTENT_NO_IMAGE_KEY + contentBean.getName());
				redisConnection.sAdd(key, getByte(contentBean.getId()));
				return null;
			}
		});
	}
}
