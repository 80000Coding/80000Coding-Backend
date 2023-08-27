package io.oopy.coding.global.security;


import io.oopy.coding.domain.user.entity.RoleType;
import io.oopy.coding.domain.user.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {
    private Long userId;
    private String username;
    private RoleType roleType;

    private CustomUserDetails(Long userId, String username, RoleType roleType) {
        this.userId = userId;
        this.username = username;
        this.roleType = roleType;
    }

    public static UserDetails of(User user) {
        return new CustomUserDetails(user.getId(), user.getName(), user.getRole());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
