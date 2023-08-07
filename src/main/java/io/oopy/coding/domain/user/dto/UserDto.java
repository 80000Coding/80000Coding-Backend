package io.oopy.coding.domain.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.oopy.coding.domain.user.domain.RoleType;
import io.oopy.coding.domain.user.domain.RoleTypeDeserializer;
import io.oopy.coding.domain.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString(of = {"id", "name", "email", "githubId", "role"})
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Integer githubId;
    @JsonDeserialize(using = RoleTypeDeserializer.class)
    private RoleType role;

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.githubId = user.getGithubId();
        this.role = user.getRole();
    }

    public User toEntity() {
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .githubId(githubId)
                .role(role)
                .build();
    }
}
