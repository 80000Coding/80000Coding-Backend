package io.oopy.coding.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserFeedSearchReq {

    @Builder
    @AllArgsConstructor
    @Getter
    @Schema(description = "유저 검색 - 닉네임으로")
    public static class Nickname {
        private String nickname;
    }
}
