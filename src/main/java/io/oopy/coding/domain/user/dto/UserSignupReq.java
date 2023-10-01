package io.oopy.coding.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = {"name"})
public class UserSignupReq {
    @NotBlank(message = "name은 필수 입력 값입니다.")
    private String name;

    public UserSignupReq() {

    }
    @Builder
    public UserSignupReq(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
