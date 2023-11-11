package io.oopy.coding.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@ToString(of = {"nickname"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserNicknameReq {

    @NotBlank(message = "nickname은 필수 입력 값입니다.")
    private String nickname;

    @Builder
    public UserNicknameReq(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }
}