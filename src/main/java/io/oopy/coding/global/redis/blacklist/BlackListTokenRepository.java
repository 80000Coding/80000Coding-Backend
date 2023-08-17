package io.oopy.coding.global.redis.blacklist;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableRedisRepositories
public class BlackListTokenRepository {
    private final RedisTemplate<Object, Object> redisTemplate;

    public BlackListTokenRepository(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(BlackListToken blackListToken) {
        String key = blackListToken.getAccess();
        Integer userId = blackListToken.getUserId();

        ValueOperations<Object, Object> valueOperation = redisTemplate.opsForValue();
        valueOperation.set(key, userId);
        redisTemplate.expire(key, blackListToken.getTtl(), java.util.concurrent.TimeUnit.SECONDS);
    }




}
