package io.oopy.coding.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.oopy.coding.common.security.jwt.dto.Jwt;

public record UserSignRes(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String action,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long userId,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Integer githubId,
        @JsonIgnore
        Jwt jwt
) {
    public static UserSignRes ofSignUp(Integer githubId, Jwt jwt) {
        return new UserSignRes("signup", null, githubId, jwt);
    }

    public static UserSignRes ofLogin(Long userId, Jwt jwt) {
        return new UserSignRes("login", userId, null, jwt);
    }

    public static UserSignRes ofAuth(Long userId, Jwt jwt) {
        return new UserSignRes(null, userId, null, jwt);
    }
}
