package io.oopy.coding.global.redis.refresh;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("refreshToken")
public class RefreshToken {
    @Id
    private final Long userId;
    private final String token;

    private RefreshToken(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public static RefreshToken of(Long userId, String refreshToken) {
        return new RefreshToken(userId, refreshToken);
    }

    public Long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}