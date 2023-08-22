package io.oopy.coding.global.redis.forbidden;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface ForbiddenTokenRepository extends CrudRepository<ForbiddenToken, String> {
}
