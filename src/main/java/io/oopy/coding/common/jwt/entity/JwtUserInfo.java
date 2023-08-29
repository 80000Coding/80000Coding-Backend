package io.oopy.coding.common.jwt.entity;

import io.oopy.coding.domain.user.entity.RoleType;
import io.oopy.coding.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
public record JwtUserInfo(
        Long id,
        Integer githubId,
        RoleType role
) {
    public static JwtUserInfo of(Long id, Integer githubId, RoleType role) {
        return new JwtUserInfo(id, githubId, role);
    }

    public static JwtUserInfo from(User user) {
        return new JwtUserInfo(user.getId(), user.getGithubId(), user.getRole());
    }

    @Override public String toString() {
        return String.format("JwtUserInfo(id=%d, githubId=%d, role=%s)", id, githubId, role);
    }
}