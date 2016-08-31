package com.ggj.webmagic.tieba.service;

import static com.ggj.webmagic.WebmagicService.TIEBA_NAME_KEY;
import static com.ggj.webmagic.WebmagicService.getByte;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ggj.webmagic.WebmagicService;
import com.ggj.webmagic.autoconfiguration.TieBaConfiguration;
import com.ggj.webmagic.autoconfiguration.TieBaImageIdMessageListener;
import com.ggj.webmagic.tieba.bean.TieBaImage;
import com.ggj.webmagic.tieba.dao.TieBaImageMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author:gaoguangjin
 * @date 2016/8/30 17:37
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class TiBaImageService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private TieBaConfiguration tieBaConfiguration;
    @Autowired
    private TieBaImageMapper tieBaImageMapper;

    /**
     * redis hgetAll 有性能问题，慎用！！！！！
     */
    @Transactional(readOnly = false)
    public void sychReisToMySql() {
        long beginTime = System.currentTimeMillis();
        //贴吧名称存放到redis
        List<TieBaImage> tiebaImageList = new ArrayList<>();
        Set<String> setTieBaName = redisTemplate.opsForSet().members(TIEBA_NAME_KEY);
        redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                for (String tieBaName : setTieBaName) {
                    byte[] imageKey = getByte(TieBaImageIdMessageListener.TIEBA_CONTENT_IMAGE_KEY + tieBaName);
                    long bime = System.currentTimeMillis();
                    Map<byte[], byte[]> mapByte = redisConnection.hGetAll(imageKey);
                    log.info(mapByte.size() + "数据，hGetAll耗时：" + (System.currentTimeMillis() - bime) / 1000 + "s");
                    for (Iterator<byte[]> iter = mapByte.keySet().iterator(); iter.hasNext(); ) {
                        byte[] pageId = iter.next();
                        byte[] imageUrl = mapByte.get(pageId);
                        if (imageUrl != null) {
                            tiebaImageList.add(new TieBaImage(WebmagicService.getString(pageId), WebmagicService.getString(imageUrl), tieBaName));
                        }
                    }
                }
                return null;
            }
        });
        log.info("redis循环总耗时：" + (System.currentTimeMillis() - beginTime) / 1000 + "s");
        log.info("待同步入库数据大小：" + tiebaImageList.size());
        beginTime = System.currentTimeMillis();
        threadInsert(tiebaImageList);
        long endIime = System.currentTimeMillis();
        log.info("数据库插入耗时：" + (endIime - beginTime) / 1000 + "s");
    }
    private void threadInsert(List<TieBaImage> tiebaImageList) {
        int pageSize = 1000;
        int num = tiebaImageList.size() / pageSize;
        ExecutorService pool = Executors.newFixedThreadPool(200);
        for (int i = 0; i < num; i++) {
            List<TieBaImage> list = new ArrayList<>();
            if (i == num - 1) {
                list = tiebaImageList.subList(pageSize * i, tiebaImageList.size() - 1);
            } else {
                list = tiebaImageList.subList(pageSize * i, pageSize * (i + 1));
            }
            pool.execute(startThread(list));
        }
        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            log.error("pool await error"+e.getLocalizedMessage());
        }
    }

    private Runnable startThread(List<TieBaImage> list) {
        return new Thread() {
            public void run() {
                long beginIime = System.currentTimeMillis();
                List<TieBaImage> notExistList = new ArrayList<>();
                for (TieBaImage tieBaImage : list) {
                    //防止重复插入
                    long singleBeginIime = System.currentTimeMillis();
                    Integer count = tieBaImageMapper.selectCountByPageId(tieBaImage.getPageId());
                    if (count == 0)
                        notExistList.add(tieBaImage);
                    long singleEndIime = System.currentTimeMillis();
                }
                long endIime = System.currentTimeMillis();
                log.info(list.size()+"数据查询耗时：" + (endIime - beginIime)/1000  + "s");
                tieBaImageMapper.insertBatch(notExistList);

            }
        };
    }
}
