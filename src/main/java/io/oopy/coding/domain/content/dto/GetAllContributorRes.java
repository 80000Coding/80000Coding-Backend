package io.oopy.coding.domain.content.dto;

import io.oopy.coding.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "컨트리뷰터 조회 응답 DTO")
public class GetAllContributorRes {
    private String name;
    private String profileImageUrl;

    public static GetAllContributorRes fromUser(User user) {
        return GetAllContributorRes.builder()
                .name(user.getName())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
