package com.ggj.webmagic.autoconfiguration;

import static com.ggj.webmagic.WebmagicService.getByte;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.ggj.webmagic.elasticsearch.ElasticSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

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
	private ElasticSearch elasticSearch;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 所有帖子ID，部分帖子包含图片
	 * @param message
	 * @param bytes
     */
	public void onMessage(Message message, byte[] bytes) {
		String jsonStr = WebmagicService.getString(message.getBody());
		List<ContentBean> pageList = JSONObject.parseArray(jsonStr, ContentBean.class);
		if (pageList != null && pageList.size() > 0) {
			String tiebaName = pageList.get(0).getName();
			//包含图片的key
			byte[] imageKey = getByte(TIEBA_CONTENT_IMAGE_KEY + tiebaName);
			//不包含图片的key
			byte[] noImageKey = getByte(TieBaNoImageIdMessageListener.TIEBA_CONTENT_NO_IMAGE_KEY + tiebaName);
			log.info("{}:待同步图片pageID数量：{}" ,tiebaName, pageList.size());
			//去除不含图片的list
			List<ContentBean> imagePageList = new ArrayList<>();
			//去除不含图片没有被缓存的list
			List<String> notCachePageImageList = new ArrayList<>();
			removeExistId(pageList, imagePageList, notCachePageImageList, imageKey, noImageKey);
			log.info("{}:去除已同步和不包含图片pageID后数量：{}",tiebaName ,notCachePageImageList.size());
			ConcurrentHashMap<byte[], byte[]> map = contentImageProcessor.start(notCachePageImageList,tiebaName);
			redisTemplate.executePipelined(new RedisCallback<Object>() {
				@Override
				public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
					//保存所有帖子 tieba_content_image_4813146001
					if(map.size()>0)
					redisConnection.mSet(map);
					// 存储帖子最新更新时间
					byte[] timeKey = getByte(TIEBA_CONTENT_UPDATE_TIME_KEY + tiebaName);
					for (ContentBean contentBean : imagePageList) {
						Set<String> set = CollectionUtils.convertMapByteToSetString(map);
						if (set.contains(TIEBA_CONTENT_IMAGE_KEY+contentBean.getId())){
							//key=tieba_content_image_李毅
							redisConnection.sAdd(imageKey,WebmagicService.getByte(contentBean.getId()));
							//key=tieba_content_image_4813146001
							redisConnection.zAdd(timeKey, Double.parseDouble(contentBean.getDate()), WebmagicService.getByte(contentBean.getId()));
						}
					}
					return null;
				}
			});
		}
	}

	/**
	 * 优化去除已经爬过的和不存在图片的pageid
	 * @param list
	 * @param imagePageList  包含图片的帖子（用来更新时间）
	 * @param notCachePageImageList 包含图片且没有被缓存进去的帖子
	 * @param imageKey
     * @param noImageKey
     */
	private void removeExistId(List<ContentBean> list, List<ContentBean> imagePageList, List<String> notCachePageImageList,
		byte[] imageKey, byte[] noImageKey) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
				// 已经缓存过包含图片的帖子Id
				Set<String> cacheImageSet = CollectionUtils.convertSetByteToSetString(redisConnection.sMembers(imageKey));
				// 不包含图片的帖子Id
				Set<String> noImageSet = CollectionUtils.convertSetByteToSetString(redisConnection.sMembers(noImageKey));
				for(ContentBean content : list) {
					try {
						//标题放入elasticsearch里面搜索
						elasticSearch.getBeanBlockingDeque().put(content);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					String id = content.getId();
					if (!noImageSet.contains(id)) {
						imagePageList.add(content);
						if (!cacheImageSet.contains(id))
							notCachePageImageList.add(id);
					}
				}
				return null;
			}
		});
	}
}
