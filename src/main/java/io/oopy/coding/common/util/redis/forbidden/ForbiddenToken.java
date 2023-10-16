package io.oopy.coding.common.util.redis.forbidden;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash("forbiddenToken")
public class ForbiddenToken {
    @Id
    private final String accessToken;
    private final Integer githubId;
    @TimeToLive
    private final long ttl;

    @Builder
    private ForbiddenToken(String accessToken, Integer githubId, long ttl) {
        this.accessToken = accessToken;
        this.githubId = githubId;
        this.ttl = ttl;
    }

    public static ForbiddenToken of(String accessToken, Integer githubId, long ttl) {
        return new ForbiddenToken(accessToken, githubId, ttl);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ForbiddenToken that)) return false;
        return accessToken.equals(that.accessToken) && githubId.equals(that.githubId);
    }

    @Override public int hashCode() {
        int result = accessToken.hashCode();
        result = ((1 << 5) - 1) * result + githubId.hashCode();
        return result;
    }
}
