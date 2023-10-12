package io.oopy.coding.common.util.redis.signupRefresh;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("refreshSignupToken")
@Getter
@ToString(of = {"githubId", "token"})
public class RefreshSignupToken {
    @Id
    private final Integer githubId;
    private String token;
    @TimeToLive
    private final long ttl;

    @Builder
    private RefreshSignupToken(String token, Integer githubId, long ttl) {
        this.token = token;
        this.githubId = githubId;
        this.ttl = ttl;
    }

    protected void rotation(String token) {
        this.token = token;
    }
}