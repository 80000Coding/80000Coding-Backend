package io.oopy.coding.common.redis.organizationcert;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class OrganizationCertification {

    private String id;
    private String certToken;
    private String userEmail;
    private String organizationCode;
    private String organizationName;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime expiredAt;
    private Boolean expired;

    @Builder
    private OrganizationCertification(String certToken, String id, String userEmail,
                                      String organizationCode, String organizationName, long ttl) {
        this.certToken = certToken;
        this.id = id;
        this.userEmail = userEmail;
        this.organizationCode = organizationCode;
        this.organizationName = organizationName;
        this.expiredAt = LocalDateTime.now().plus(ttl, ChronoUnit.SECONDS);
    }

    public static OrganizationCertification of(String certToken, String id, String userEmail,
                                               String organizationCode, String organizationName, long ttl) {
        return new OrganizationCertification(certToken, id, userEmail,
                                             organizationCode, organizationName, ttl);
    }

    public boolean isExpired() {
        return this.expiredAt.isAfter(LocalDateTime.now());
    }
}
