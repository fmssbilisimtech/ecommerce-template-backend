package com.fmss.commondata.redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    @Value("${redis.service.prefix}")
    private String redisDomainPrefix;
    @Value("${redis.service.ttl}")
    private long redisSessionTimeToLive;

    @Autowired
    public RedisCacheService(@Qualifier("fmssRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Async
    public <T> void writeUserToLocalCache(String key, String hashKey, T object) {
        this.redisTemplate.opsForHash().put(this.redisDomainPrefix.concat(key), hashKey, object);
        this.redisTemplate.expire(this.redisDomainPrefix.concat(key), this.redisSessionTimeToLive, TimeUnit.SECONDS);
    }



    public <T> T getPartialSessionData(String key, String hashKey, Class<T> targetClass){
        return targetClass.cast(redisTemplate.opsForHash().get(redisDomainPrefix.concat(key), hashKey));
    }

    public void removeUserTokenFromRedis(String key){
        this.redisTemplate.opsForHash().getOperations().delete(this.redisDomainPrefix.concat(key));

    }


}
