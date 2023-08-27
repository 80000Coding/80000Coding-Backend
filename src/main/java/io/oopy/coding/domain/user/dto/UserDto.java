package io.oopy.coding.domain.user.dto;

import io.oopy.coding.domain.user.entity.RoleType;
import io.oopy.coding.domain.user.entity.User;
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
