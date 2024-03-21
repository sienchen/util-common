package com.tongtu.cyber.util.redis;

import cn.hutool.core.util.ObjUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Collections;

/***
 * redis配置类，开始缓存和声明配置
 */
@EnableCaching
//@Configuration
@Log4j2
public class RedisBean extends CachingConfigurerSupport {
    //LettuceConnectionFactory 是 Lettuce（一个 Redis 客户端库）的连接工厂
    @Resource
    private LettuceConnectionFactory factory;

    public RedisBean(LettuceConnectionFactory factory) {
        if (ObjUtil.isEmpty(factory)) {
            log.error("redis初始化错误，LettuceConnectionFactory cannot be null");
            throw new RuntimeException("LettuceConnectionFactory cannot be null");
        }
        this.factory = factory;
    }

    /**
     * redis连接初始化,直接获取连接工厂的参数
     *
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory factory) {
        log.info(" --- redis config init --- ");
        //创建 Jackson2JsonRedisSerializer
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
        om.enableDefaultTyping(DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // 创建 RedisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        RedisSerializer<?> stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        //完成 RedisTemplate 的初始化
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 开启缓存配置
     *
     * @param factory
     * @return
     */
    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(6L));
        RedisCacheConfiguration redisCacheConfiguration = config.serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer())).serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        RedisCacheManager cacheManager = RedisCacheManager.builder(RedisCacheWriter.lockingRedisCacheWriter(factory)).cacheDefaults(redisCacheConfiguration).withInitialCacheConfigurations(Collections.singletonMap("test:demo", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5L)).disableCachingNullValues())).withInitialCacheConfigurations(Collections.singletonMap("pluginMall::rankingList", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(24L)).disableCachingNullValues())).withInitialCacheConfigurations(Collections.singletonMap("pluginMall::queryPageList", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(24L)).disableCachingNullValues())).transactionAware().build();
        return cacheManager;
    }
}
