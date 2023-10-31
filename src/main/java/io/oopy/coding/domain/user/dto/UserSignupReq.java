package io.oopy.coding.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@ToString(of = {"name"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSignupReq {
    @NotBlank(message = "name은 필수 입력 값입니다.")
    private String name;

    @Builder
    public UserSignupReq(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
