package io.oopy.coding.global.redis.refresh;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("refreshToken")
@Getter
@ToString(of = {"access", "refresh", "userId"})
public class RefreshToken {
    @Id
    private String accessToken;
    private final String refreshToken;
    private final Long userId;
    @TimeToLive
    private final long ttl;

    @Builder
    private RefreshToken(String accessToken, String refreshToken, Long userId, long ttl) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.ttl = ttl;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}