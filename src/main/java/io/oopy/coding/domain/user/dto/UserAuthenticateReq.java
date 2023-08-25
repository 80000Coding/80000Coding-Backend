package io.oopy.coding.domain.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.oopy.coding.domain.user.domain.RoleType;
import io.oopy.coding.domain.user.domain.RoleTypeDeserializer;
import io.oopy.coding.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = {"id", "githubId"})
public class UserAuthenticateReq {
    private Long id;
    private Integer githubId;
    private RoleType role;

    @Builder
    private UserAuthenticateReq(Long id, Integer githubId, RoleType role) {
        this.id = id;
        this.githubId = githubId;
        this.role = role;
    }

    public static UserAuthenticateReq of(Long id, Integer githubId, RoleType role) {
        return new UserAuthenticateReq(id, githubId, role);
    }

    public static UserAuthenticateReq from(User user) {
        return UserAuthenticateReq.builder()
                .id(user.getId())
                .githubId(user.getGithubId())
                .role(user.getRole())
                .build();
    }
}
