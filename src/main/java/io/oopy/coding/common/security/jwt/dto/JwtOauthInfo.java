package io.oopy.coding.common.security.jwt.dto;

import io.oopy.coding.domain.user.entity.RoleType;
import lombok.Builder;

@Builder
public record JwtOauthInfo(
        Integer id,
        RoleType role
) {
    public static JwtOauthInfo of(Integer id, RoleType role) {
        return JwtOauthInfo.builder()
                .id(id)
                .role(role)
                .build();
    }
}
