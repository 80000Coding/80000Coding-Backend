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
    private final Long id;
    @TimeToLive
    private final long ttl;

    @Builder
    private ForbiddenToken(String accessToken, Long id, long ttl) {
        this.accessToken = accessToken;
        this.id = id;
        this.ttl = ttl;
    }

    public static ForbiddenToken of(String accessToken, Long id, long ttl) {
        return new ForbiddenToken(accessToken, id, ttl);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ForbiddenToken that)) return false;
        return accessToken.equals(that.accessToken) && id.equals(that.id);
    }

    @Override public int hashCode() {
        int result = accessToken.hashCode();
        result = ((1 << 5) - 1) * result + id.hashCode();
        return result;
    }
}
