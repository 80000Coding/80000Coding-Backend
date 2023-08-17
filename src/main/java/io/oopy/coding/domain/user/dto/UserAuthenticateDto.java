package io.oopy.coding.domain.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.oopy.coding.domain.user.domain.RoleType;
import io.oopy.coding.domain.user.domain.RoleTypeDeserializer;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = {"id", "githubId"})
public class UserAuthenticateDto {
    private Long id;
    private Integer githubId;
    @JsonDeserialize(using = RoleTypeDeserializer.class)
    private RoleType role;

    private UserAuthenticateDto(Long id, RoleType role) {
        this.id = id;
        this.role = role;
    }

    public static UserAuthenticateDto of(Long id, RoleType role) {
        return new UserAuthenticateDto(id, role);
    }
}