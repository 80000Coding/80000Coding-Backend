package io.oopy.coding.global.security;


import io.oopy.coding.domain.user.entity.RoleType;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.global.jwt.entity.JwtUserInfo;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

@Getter
public final class CustomUserDetails implements UserDetails {
    private final Long userId;
    private final Integer githubId;
    private final RoleType role;

    @Builder
    private CustomUserDetails(Long userId, Integer githubId, RoleType role) {
        this.userId = userId;
        this.githubId = githubId;
        this.role = role;
    }

    public static UserDetails of(User user) {
        return new CustomUserDetails(user.getId(), user.getGithubId(), user.getRole());
    }

    public JwtUserInfo toJwtUserInfo() {
        return JwtUserInfo.builder()
                .id(userId)
                .githubId(githubId)
                .role(role)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(RoleType.values())
                .filter(roleType -> roleType == role)
                .map(roleType -> (GrantedAuthority) roleType::name)
                .toList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userId.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
