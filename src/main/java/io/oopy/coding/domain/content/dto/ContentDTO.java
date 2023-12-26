package io.oopy.coding.domain.content.dto;

import io.oopy.coding.domain.content.entity.Content;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ContentDTO {
    private Long id;
    private String title;
    private String body;
    private String type;
    private Long views;
    private String repoName;
    private String repoOwner;
    private Boolean publish;
    private String contentImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static ContentDTO toDTO(Content content) {
        return ContentDTO.builder()
                .id(content.getId())
                .title(content.getTitle())
                .body(content.getBody())
                .type(content.getType().getType())
                .views(content.getViews())
                .repoName(content.getRepoName())
                .repoOwner(content.getRepoOwner())
                .publish(content.getPublish())
                .contentImageUrl(content.getContentImageUrl())
                .createdAt(content.getCreatedAt())
                .updatedAt(content.getUpdatedAt())
                .deletedAt(content.getDeleteAt())
                .build();
    }
}
