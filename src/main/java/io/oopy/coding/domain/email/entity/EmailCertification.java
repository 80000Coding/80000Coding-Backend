package io.oopy.coding.domain.email.entity;

import io.oopy.coding.common.constant.EmailConstant;
import io.oopy.coding.domain.entity.Auditable;
import jakarta.persistence.*;
import lombok.*;

import static io.oopy.coding.common.util.Time.getNowUnixTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "EMAIL_CERTIFICATION")
public class EmailCertification extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String githubId;

    @Column
    private Integer expiredAt; // unix time

    @Column
    private Boolean verify;

    public boolean isExpired() {
        return getNowUnixTime() > expiredAt;
    }

    public static EmailCertification of(String email, String githubId) {
        Integer expireTime = getNowUnixTime() + EmailConstant.EMAIL_EXPIRED_PERIOD_SECONDS;
        return EmailCertification.builder()
                .email(email)
                .githubId(githubId)
                .expiredAt(expireTime)
                .verify(Boolean.FALSE)
                .build();
    }
}
