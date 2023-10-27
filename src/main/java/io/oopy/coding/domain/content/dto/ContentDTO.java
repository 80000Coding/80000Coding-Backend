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
    private String repo_name;
    private String repo_owner;
    private boolean complete;
    private String content_image_url;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime deleted_at;

    public static ContentDTO toDTO(Content content) {
        return ContentDTO.builder()
                .id(content.getId())
                .title(content.getTitle())
                .body(content.getBody())
                .type(content.getType())
                .views(content.getViews())
                .repo_name(content.getRepoName())
                .repo_owner(content.getRepoOwner())
                .complete(content.isComplete())
                .content_image_url(content.getContentImageUrl())
                .created_at(content.getCreatedAt())
                .updated_at(content.getUpdatedAt())
                .deleted_at(content.getDeleteAt())
                .build();
    }
}
