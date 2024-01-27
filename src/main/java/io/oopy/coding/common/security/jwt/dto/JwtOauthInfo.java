package io.oopy.coding.common.security.jwt.dto;

import io.oopy.coding.domain.user.entity.RoleType;
import io.oopy.coding.domain.user.entity.User;
import lombok.Builder;

@Builder
public record JwtOauthInfo(
        Integer githubId,
        RoleType role
) implements JwtSubInfo {
    public static JwtSubInfo of(Integer githubId, RoleType role) {
        return JwtOauthInfo.builder()
                .githubId(githubId)
                .role(role)
                .build();
    }

    public static JwtSubInfo from(User user) {
        return JwtOauthInfo.builder()
                .githubId(user.getGithubId())
                .role(user.getRole())
                .build();
    }

    @Override
    public Long id() {
        throw new UnsupportedOperationException();
    }
}
