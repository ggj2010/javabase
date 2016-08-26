package com.ggj.webmagic.autoconfiguration;

import com.ggj.webmagic.util.QiNiuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import static javafx.scene.input.KeyCode.T;

/**
 * @author:gaoguangjin
 * @date 2016/8/22 18:45
 */
@Configuration
public class BeanConfiguration {
    @Autowired
    TieBaConfiguration tieBaConfiguration;
    @Autowired
    TieBaImageIdMessageListener TieBaImageIdMessageListener;
    @Autowired
    TieBaNoImageIdMessageListener tieBaNoImageIdMessageListener;
    /**
     * spring-data-redis 使用jedsi作为实现类
     * @param jedisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, String>  stringRedisTemplate(JedisConnectionFactory jedisConnectionFactory){
        RedisTemplate<String, String>  redisTemplate=new RedisTemplate<String, String> ();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        return redisTemplate;
    }
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(JedisConnectionFactory jedisConnectionFactory){
        RedisMessageListenerContainer  redisMessageListenerContainer=new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(jedisConnectionFactory);
        redisMessageListenerContainer.addMessageListener(TieBaImageIdMessageListener,new ChannelTopic(tieBaConfiguration.getTiebaContentIdTopic()));
        redisMessageListenerContainer.addMessageListener(tieBaNoImageIdMessageListener,new ChannelTopic(tieBaConfiguration.getTiebaContentNoImageIdTopic()));
        return redisMessageListenerContainer;
    }
}
