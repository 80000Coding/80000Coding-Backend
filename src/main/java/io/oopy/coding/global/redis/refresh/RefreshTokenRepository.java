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
    private final RedisTemplate<Long, String> redisTemplate;

    public RefreshTokenRepository(RedisTemplate<Long, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(RefreshToken refreshToken, int refreshExpireTime) {
        Long key = refreshToken.getUserId();
        String token = refreshToken.getToken();

        ValueOperations<Long, String> valueOperation = redisTemplate.opsForValue();
        valueOperation.set(key, token);
        redisTemplate.expire(key, refreshExpireTime, TimeUnit.HOURS);
    }

    public Optional<RefreshToken> findById(Long userId) {
        ValueOperations<Long, String> valueOperation = redisTemplate.opsForValue();
        String refreshToken = valueOperation.get(userId);

        return (Objects.isNull(refreshToken))
                ? Optional.empty()
                : Optional.of(RefreshToken.of(userId, refreshToken));
    }
}