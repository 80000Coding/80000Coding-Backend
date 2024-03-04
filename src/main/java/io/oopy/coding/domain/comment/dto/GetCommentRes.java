package io.oopy.coding.domain.comment.dto;

import io.oopy.coding.domain.comment.entity.Comment;
import io.oopy.coding.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class GetCommentRes {
    private List<ResComment> comments;

    @Builder
    @Getter
    public static class ResComment {
        private Long id;
        private String body;
        private Long parentId;
        private String userName;
        private String userProfileImageUrl;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime deletedAt;

        public static ResComment from(Comment comment) {
            return ResComment.builder()
                    .id(comment.getId())
                    .body(comment.getCommentBody())
                    .userName(comment.getUser().getName())
                    .userProfileImageUrl(comment.getUser().getProfileImageUrl())
                    .parentId(comment.getParentId())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .deletedAt(comment.getDeleteAt())
                    .build();
        }
    }

    public static GetCommentRes from(List<Comment> comments) {
        GetCommentRes response = GetCommentRes.builder()
                .comments(new ArrayList<>())
                .build();

        for (Comment comment : comments) {
            response.comments.add(ResComment.from(comment));
        }

        return response;
    }

}
