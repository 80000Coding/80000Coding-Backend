package io.oopy.coding.global.redis.refresh;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@EnableRedisRepositories
public class RefreshTokenRepository {
    private final RedisTemplate<Object, Object> redisTemplate;

    public RefreshTokenRepository(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(RefreshToken refreshToken, int refreshExpireTime) {
        Long key = refreshToken.getUserId();
        String token = refreshToken.getToken();

        ValueOperations<Object, Object> valueOperation = redisTemplate.opsForValue();
        valueOperation.set(key, token);
        redisTemplate.expire(key, refreshExpireTime, TimeUnit.HOURS);
    }

    public Optional<RefreshToken> findById(Long userId) {
        ValueOperations<Object, Object> valueOperation = redisTemplate.opsForValue();
        String refreshToken = Objects.requireNonNull(valueOperation.get(userId)).toString();

        return (Objects.isNull(refreshToken))
                ? Optional.empty()
                : Optional.of(RefreshToken.of(userId, refreshToken));
    }
}