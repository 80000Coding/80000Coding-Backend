package io.oopy.coding.common.security.jwt.dto;

import io.oopy.coding.domain.user.entity.RoleType;
import io.oopy.coding.domain.user.entity.User;
import lombok.*;

import static io.oopy.coding.domain.user.entity.RoleType.USER;

@Builder
public record JwtUserInfo(
        Long id,
        Integer githubId,
        RoleType role
) {
    public static JwtUserInfo of(Long id, Integer githubId, RoleType role) {
        return new JwtUserInfo(id, githubId, role);
    }

    public static JwtUserInfo createByGithubId(Integer githubId) {
        return new JwtUserInfo(-1L, githubId, USER);
    }

    public static JwtUserInfo from(User user) {
        return new JwtUserInfo(user.getId(), user.getGithubId(), user.getRole());
    }

    @Override public String toString() {
        return String.format("JwtUserInfo(id=%d, githubId=%d, role=%s)", id, githubId, role);
    }
}