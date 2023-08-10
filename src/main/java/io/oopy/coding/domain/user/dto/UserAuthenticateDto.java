package io.oopy.coding.domain.user.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = {"id", "githubId"})
public class UserAuthenticateDto {
    private Long id;
    private Integer githubId;

    private UserAuthenticateDto(Long id, Integer githubId) {
        this.id = id;
        this.githubId = githubId;
    }

    public static UserAuthenticateDto of(Long id, Integer githubId) {
        return new UserAuthenticateDto(id, githubId);
    }
}