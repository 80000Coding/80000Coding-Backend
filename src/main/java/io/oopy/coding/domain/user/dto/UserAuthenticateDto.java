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
public class UserAuthenticateDto {
    private Long id;
    private Integer githubId;
    @JsonDeserialize(using = RoleTypeDeserializer.class)
    private RoleType role;

    @Builder
    private UserAuthenticateDto(Long id, Integer githubId, RoleType role) {
        this.id = id;
        this.githubId = githubId;
        this.role = role;
    }

    public static UserAuthenticateDto of(Long id, Integer githubId, RoleType role) {
        return new UserAuthenticateDto(id, githubId, role);
    }

    public static UserAuthenticateDto newInstance(User user) {
        return UserAuthenticateDto.builder()
                .id(user.getId())
                .githubId(user.getGithubId())
                .role(user.getRole())
                .build();
    }
}
