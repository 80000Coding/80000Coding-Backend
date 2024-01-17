package io.oopy.coding.common.security.jwt.dto;

import io.oopy.coding.domain.user.entity.RoleType;
import io.oopy.coding.domain.user.entity.User;
import lombok.Builder;

@Builder
public record JwtAuthInfo(
        Long id,
        RoleType role
) {
    public static JwtAuthInfo of(Long id, RoleType role) {
        return JwtAuthInfo.builder()
                .id(id)
                .role(role)
                .build();
    }

    public static JwtAuthInfo from(User user) {
        return JwtAuthInfo.builder()
                .id(user.getId())
                .role(user.getRole())
                .build();
    }
}
