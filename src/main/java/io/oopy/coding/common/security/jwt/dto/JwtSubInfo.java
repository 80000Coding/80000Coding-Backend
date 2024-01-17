package io.oopy.coding.common.security.jwt.dto;

import io.oopy.coding.domain.user.entity.RoleType;

public interface JwtSubInfo {
    Long id();
    String oauthId();
    RoleType role();
    String phoneNumber();
}

