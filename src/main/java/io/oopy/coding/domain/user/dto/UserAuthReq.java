package io.oopy.coding.domain.user.dto;

import io.oopy.coding.domain.user.entity.RoleType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(of = {"id", "githubId"})
public class UserAuthReq {
    @NotBlank(message = "id는 필수 입력 값입니다.")
    private Long id;
    @NotBlank(message = "githubId는 필수 입력 값입니다.")
    private Integer githubId;
    @NotBlank(message = "role은 필수 입력 값입니다.")
    private RoleType role;

    @Builder
    public UserAuthReq(Long id, Integer githubId, RoleType role) {
        this.id = id;
        this.githubId = githubId;
        this.role = role;
    }
}
