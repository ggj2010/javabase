package com.ggj.webmagic;


import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSONObject;
import com.ggj.webmagic.autoconfiguration.TieBaConfiguration;
import com.ggj.webmagic.autoconfiguration.TieBaImageIdMessageListener;
import com.ggj.webmagic.elasticsearch.ElasticSearch;
import com.ggj.webmagic.tieba.ContentIdProcessor;
import com.ggj.webmagic.tieba.TopProcessor;
import com.ggj.webmagic.tieba.bean.ContentBean;
import com.ggj.webmagic.tieba.bean.TopBean;
import com.ggj.webmagic.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

import static freemarker.log._Log4jOverSLF4JTester.test;
import static org.apache.coyote.http11.Constants.a;


/**
 * @author:gaoguangjin
 * @date 2016/8/19 11:17
 */
@Service
@Slf4j
public class WebmagicService {
    public static final String TIEBA_TOP_KEY = "tieba_top_";
    public static final String TIEBA_NAME_KEY = "tieba_names";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ElasticSearch elasticSearch;

    @Autowired
    private TieBaConfiguration tieBaConfiguration;

    @Autowired
    private TopProcessor topProcessor;
    @Autowired
    private ContentIdProcessor contentIdProcessor;

    public static byte[] getByte(String str) {
        try {
            return str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("getByte error:" + e.getLocalizedMessage());
        }
        return null;
    }

    public static String getString(byte[] bytes) {
        try {
            return new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("getString error:" + e.getLocalizedMessage());
        }
        return null;
    }

    public void getTieBaTop(String tieBaName, Integer size, Model model) {
        List<String> xAxis = new ArrayList<>();
        List<String> series = new ArrayList<>();
        Set<byte[]> set = getTieBaTopFromRedis(tieBaName, size);
        for (Iterator<byte[]> iterator = set.iterator(); iterator.hasNext(); ) {
            TopBean topBeans = JSONObject.parseObject(getString(iterator.next()), TopBean.class);
            xAxis.add(topBeans.getUserName());
            series.add(topBeans.getExperience());
        }
        model.addAttribute("xAxis", xAxis);
        model.addAttribute("series", series);
    }

    /**
     * 根据等级分组
     *
     * @param tieBaName
     * @param model
     */
    public void getLevelCount(String tieBaName, Model model) {
        List<Integer> xAxis = new ArrayList<>();
        List<Integer> series = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        Set<byte[]> set = getTieBaTopFromRedis(tieBaName, null);
        for (Iterator<byte[]> iterator = set.iterator(); iterator.hasNext(); ) {
            TopBean topBeans = JSONObject.parseObject(getString(iterator.next()), TopBean.class);
            Integer level = Integer.parseInt(topBeans.getLevel());
            Integer count = map.get(level);
            map.put(level, count == null ? 1 : count + 1);
        }
        for (Iterator<Integer> iter = map.keySet().iterator(); iter.hasNext(); ) {
            Integer level = iter.next();
            Integer count = map.get(level);
            xAxis.add(level);
            series.add(count);
        }
        model.addAttribute("xAxis", xAxis);
        model.addAttribute("series", series);
    }

    private Set<byte[]> getTieBaTopFromRedis(String tieBaName, Integer size) {
        return redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
            public Set<byte[]> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] key = getByte(TIEBA_TOP_KEY + tieBaName);
                if (redisConnection.exists(key)) {
                    return redisConnection.zRange(key, 0, size == null ? -1 : size);
                }
                return null;
            }
        });
    }

    /**
     * 根据贴吧名称调用tiebaTop爬虫
     *
     * @throws UnsupportedEncodingException
     */
    public void addTieBaTop() throws UnsupportedEncodingException {
        Map<String, TopBean> map = null;
        //贴吧名称存放到redis
        Set<byte[]> setTieBaName = getCacheTieBaName();
        for (byte[] bytes : setTieBaName) {
            String name = getString(bytes);
            putTieBaTopResultToRedis(name, topProcessor.start(name));
        }
    }

    private void putTieBaTopResultToRedis(String name, ConcurrentHashMap<String, TopBean> map) {
        redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] key = getByte(TIEBA_TOP_KEY + name);
                redisConnection.del(key);
                for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext(); ) {
                    String id = iter.next();
                    TopBean topBean = map.get(id);
                    redisConnection.zAdd(key, Double.parseDouble(topBean.getIndex()), getByte(JSONObject.toJSON(topBean).toString()));
                }
                return null;
            }
        });
    }

    public void addTieBaImage() {
        Set<byte[]> setTieBaName = getCacheTieBaName();
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
                byte[] key = getByte(TIEBA_NAME_KEY);
                if (!redisConnection.exists(key)) {
                    for (String name : tiebaName)
                        redisConnection.sAdd(key, getByte(name));
                }
                return redisConnection.sMembers(key);
            }
        });
    }

    /**
     * 根据帖子最后最新更新日期显示图片
     *
     * @param model
     * @param tieBaName
     * @param begin
     * @param end
     */
    public void getTieBaImage(Model model, String tieBaName, Integer begin, Integer end) {

        byte[] contenUpdateKey = getByte(TieBaImageIdMessageListener.TIEBA_CONTENT_UPDATE_TIME_KEY + tieBaName);
        redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                Map<String, List<String>> map = new TreeMap<String, List<String>>();
                Set<byte[]> sortPageIds = redisConnection.zRevRange(contenUpdateKey, begin, end);

                for (byte[] sortPageId : sortPageIds) {
                    String pagId=getString(sortPageId);
                    byte[] imageUrls=redisConnection.get(getByte(TieBaImageIdMessageListener.TIEBA_CONTENT_IMAGE_KEY+pagId));
                    map.put(pagId, JSONObject.parseObject(getString(imageUrls), ArrayList.class));
                }
                model.addAttribute("mapData", map);
                return null;
            }
        });

    }

    /**
     * 增加分页
     *
     * @param listIds
     * @param begin
     * @param end
     */
    private List<String> setPageTolistIds(List<String> listIds, Integer begin, Integer end) {
        if (listIds != null) {
            int listSize = listIds.size();
            if (begin < 0) begin = 0;
            if (begin > listSize) begin = listSize - 1;
            if (begin > end) end = begin;
            return listIds.subList(begin, end);
        }
        return new ArrayList<>();
    }

    public List<ContentBean> search(String keyWord) {
        List<ContentBean> listContentBean = new ArrayList<>();
        SearchResponse response = elasticSearch.getTransportClient().prepareSearch(ElasticSearch.INDEX_NAME)
                .setTypes(ElasticSearch.TIEABA_CONTENT_TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchQuery(ElasticSearch.TIEABA_CONTENT_FIELD, keyWord)
                ).execute().actionGet();
        SearchHits hits = response.getHits();
        for (SearchHit searchHitFields : hits.getHits()) {
            listContentBean.add(JSONObject.parseObject(searchHitFields.getSourceAsString(), ContentBean.class));
        }
        return listContentBean;
    }
}
