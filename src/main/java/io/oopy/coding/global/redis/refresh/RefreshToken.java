package io.oopy.coding.global.redis.refresh;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("refreshToken")
@Getter
@ToString(of = {"access", "refresh"})
public class RefreshToken {
    @Id
    private final String access;
    private final String refresh;
    private final Long userId;
    @TimeToLive
    private final long ttl;

    @Builder
    private RefreshToken(String access, String refresh, Long userId, long ttl) {
        this.access = access;
        this.refresh = refresh;
        this.userId = userId;
        this.ttl = ttl;
    }
}