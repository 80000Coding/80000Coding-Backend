package io.oopy.coding.domain.content.dto;

import io.oopy.coding.domain.content.entity.Content;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class UserContentRes {
    private Long id;
    private String title;
    private String body;
    private String type;
    private Long views;
    private String contentImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static UserContentRes from(Content content, String body) {
        return UserContentRes.builder()
                .id(content.getId())
                .title(content.getTitle())
                .body(body)
                .type(content.getType().getType())
                .views(content.getViews())
                .createdAt(content.getCreatedAt())
                .updatedAt(content.getUpdatedAt())
                .deletedAt(content.getDeleteAt())
                .build();
    }
}
