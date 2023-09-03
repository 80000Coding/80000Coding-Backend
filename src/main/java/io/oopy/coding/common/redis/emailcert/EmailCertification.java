package io.oopy.coding.common.redis.emailcert;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash("emailAuthenticationToken")
public class EmailCertification {
    @Id
    private String githubId;

    private String emailToken;
    @Setter
    private Boolean checked;

    @TimeToLive
    private final long ttl;

    @Builder
    private EmailCertification(String emailToken, String githubId, long ttl) {
        this.emailToken = emailToken;
        this.checked = Boolean.FALSE;
        this.githubId = githubId;
        this.ttl = ttl;
    }

    public static EmailCertification of(String emailToken, String githubId, long ttl) {
        return new EmailCertification(emailToken, githubId, ttl);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmailCertification that)) return false;
        return emailToken.equals(that.emailToken);
    }

    @Override
    public int hashCode() {
        return emailToken.hashCode();
    }
}
