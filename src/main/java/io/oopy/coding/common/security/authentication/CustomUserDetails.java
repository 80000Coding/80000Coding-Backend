package io.oopy.coding.common.security.authentication;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.oopy.coding.domain.user.entity.RoleType;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.common.util.jwt.entity.JwtUserInfo;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

@Getter
public final class CustomUserDetails implements UserDetails {
    private Long userId;
    private Integer githubId;
    private RoleType role;

    @JsonIgnore
    private boolean enabled;
    @JsonIgnore
    private boolean password;
    @JsonIgnore
    private boolean username;
    @JsonIgnore
    private boolean authorities;
    @JsonIgnore
    private boolean credentialsNonExpired;
    @JsonIgnore
    private boolean accountNonExpired;
    @JsonIgnore
    private boolean accountNonLocked;

    private CustomUserDetails() {}

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
        return JwtUserInfo.of(userId, githubId, role);
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
