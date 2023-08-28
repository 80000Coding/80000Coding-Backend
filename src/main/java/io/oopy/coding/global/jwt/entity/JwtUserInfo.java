package io.oopy.coding.global.jwt.entity;

import io.oopy.coding.domain.user.entity.RoleType;
import io.oopy.coding.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@ToString(of = {"id", "githubId", "role"})
public final class JwtUserInfo {
    @NotBlank(message = "id는 필수 입력 값입니다.")
    private Long id;
    @NotBlank(message = "githubId는 필수 입력 값입니다.")
    private Integer githubId;
    @NotBlank(message = "role은 필수 입력 값입니다.")
    private RoleType role;

    @Builder
    private JwtUserInfo(Long id, Integer githubId, RoleType role) {
        this.id = id;
        this.githubId = githubId;
        this.role = role;
    }

    public static JwtUserInfo of(Long id, Integer githubId, RoleType role) {
        return new JwtUserInfo(id, githubId, role);
    }

    public static JwtUserInfo from(User user) {
        return JwtUserInfo.builder()
                .id(user.getId())
                .githubId(user.getGithubId())
                .role(user.getRole())
                .build();
    }
}
