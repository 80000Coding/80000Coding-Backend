package io.oopy.coding.common.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtField {
    USER_ID("userId"),
    ROLE("role"),
    GITHUB_ID("githubId");

    private final String value;
}
