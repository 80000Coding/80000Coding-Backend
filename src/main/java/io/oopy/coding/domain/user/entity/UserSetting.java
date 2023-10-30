package io.oopy.coding.domain.user.entity;

import io.oopy.coding.domain.model.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name="USER_SETTING")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSetting extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email_agree", columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    private Boolean emailAgree;

    @Column(name = "push_agree", columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    private Boolean pushAgree;

    @Column(name = "contributor_ranking_mark_agree", columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    private Boolean contributorRankingMarkAgree;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void changeEmailAgree() {
        this.emailAgree = !this.emailAgree;
    }

    public void changePushAgree() {
        this.pushAgree = !this.pushAgree;
    }

    public void changeContributorRankingMarkAgree() {
        this.contributorRankingMarkAgree = !this.contributorRankingMarkAgree;
    }

    public static UserSetting from(User user) {
        return UserSetting.builder()
                .emailAgree(Boolean.FALSE)
                .pushAgree(Boolean.FALSE)
                .contributorRankingMarkAgree(Boolean.FALSE)
                .user(user)
                .build();
    }
}
