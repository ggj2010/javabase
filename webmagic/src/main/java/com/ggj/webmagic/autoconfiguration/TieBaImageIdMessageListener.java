package com.ggj.webmagic.autoconfiguration;

import static com.ggj.webmagic.WebmagicService.getByte;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ggj.webmagic.WebmagicService;
import com.ggj.webmagic.tieba.ContentImageProcessor;
import com.ggj.webmagic.tieba.bean.ContentBean;
import com.ggj.webmagic.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * redis pub/sub 订阅者
 * @author:gaoguangjin
 * @date 2016/8/24 18:06
 */
@Slf4j
@Component
public class TieBaImageIdMessageListener implements MessageListener {
	
	public static final String TIEBA_CONTENT_IMAGE_KEY = "tieba_content_image_";
	
	public static final String TIEBA_CONTENT_UPDATE_TIME_KEY = "tieba_content_update_time_";
	
	@Autowired
	private ContentImageProcessor contentImageProcessor;
	
	@Autowired
	private TieBaConfiguration tieBaConfiguration;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	public void onMessage(Message message, byte[] bytes) {
		String jsonStr = WebmagicService.getString(message.getBody());
		List<ContentBean> list = JSONObject.parseArray(jsonStr, ContentBean.class);
		// list 里面最后一条数据存放的是贴吧名称，为了兼容多个贴吧存储
		if (list != null && list.size() > 0) {
			String tiebaName = list.get(0).getName();
			byte[] imageKey = getByte(TIEBA_CONTENT_IMAGE_KEY + tiebaName);
			byte[] noImageKey = getByte(TieBaNoImageIdMessageListener.TIEBA_CONTENT_NO_IMAGE_KEY + tiebaName);
			log.info("待同步图片pageID数量：" + list.size());
			List<ContentBean> notImageList = new ArrayList<>();
			List<String> notCacheImageList = new ArrayList<>();
			removeExistId(list, notImageList, notCacheImageList, imageKey, noImageKey);
			log.info("去除已同步和不包含图片pageID后数量：" + notCacheImageList.size());
			ConcurrentHashMap<byte[], byte[]> map = contentImageProcessor.start(notCacheImageList,tiebaName);
			redisTemplate.execute(new RedisCallback<Object>() {
				@Override
				public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
					if (map.size() > 0)
						redisConnection.hMSet(imageKey, map);

					Set<String> convertSet = CollectionUtils.convertSetByteToSetString(redisConnection.hKeys(imageKey));
					// 存储帖子最新更新时间
					byte[] timeKey = getByte(TIEBA_CONTENT_UPDATE_TIME_KEY + tiebaName);
					for(ContentBean content : notImageList) {
						byte[] bytePageId = WebmagicService.getByte(content.getId());
						// 只存储有图片的帖子
						if (convertSet.contains(content.getId()))
							redisConnection.zAdd(timeKey, Double.parseDouble(content.getDate()), bytePageId);
					}
					return null;
				}
			});
		}
	}

	/**
	 * 优化去除已经爬过的和不存在图片的pageid
	 * @param list
	 * @param notImageList
	 * @param notCacheImageList
	 * @param imageKey
     * @param noImageKey
     */
	private void removeExistId(List<ContentBean> list, List<ContentBean> notImageList, List<String> notCacheImageList,
		byte[] imageKey, byte[] noImageKey) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
				// 已经缓存过包含图片的帖子Id
				Set<String> cacheImageSet = CollectionUtils.convertSetByteToSetString(redisConnection.hKeys(imageKey));
				// 不包含图片的帖子Id
				Set<String> noImageSet = CollectionUtils.convertSetByteToSetString(redisConnection.sMembers(noImageKey));
				for(ContentBean content : list) {
					String id = content.getId();
					if (!noImageSet.contains(id)) {
						notImageList.add(content);
						if (!cacheImageSet.contains(id))
							notCacheImageList.add(id);
					}
				}
				return null;
			}
		});
	}
}
