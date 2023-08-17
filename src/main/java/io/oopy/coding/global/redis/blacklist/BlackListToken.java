package io.oopy.coding.global.redis.blacklist;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash("blackListToken")
public class BlackListToken {
    @Id
    private String access;
    private Integer userId;
    @TimeToLive
    private Long ttl;

    private BlackListToken(String access, Integer userId, Long ttl) {
        this.access = access;
        this.userId = userId;
        this.ttl = ttl;
    }

    public static BlackListToken of(String access, Integer userId, Long ttl) {
        return new BlackListToken(access, userId, ttl/1000);
    }
}
