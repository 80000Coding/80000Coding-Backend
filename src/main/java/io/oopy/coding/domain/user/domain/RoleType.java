package io.oopy.coding.domain.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String role;
}