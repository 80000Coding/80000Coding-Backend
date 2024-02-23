package io.oopy.coding.domain.comment.dto;

import io.oopy.coding.domain.comment.entity.Comment;
import io.oopy.coding.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class GetCommentRes {

    private ResComment comment;
    private ResUser user;

    @Builder
    @Getter
    public static class ResComment {
        private String body;
        private Long parentId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime deletedAt;

        public static ResComment from(Comment comment) {
            return ResComment.builder()
                    .body(comment.getCommentBody())
                    .parentId(comment.getParentId())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .deletedAt(comment.getDeleteAt())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ResUser {
        private String name;
        private String profileImageUrl;

        public static ResUser from(User user) {
            return ResUser.builder()
                    .name(user.getName())
                    .profileImageUrl(user.getProfileImageUrl())
                    .build();
        }
    }

    public static GetCommentRes fromEntity(Comment comment) {
        return GetCommentRes.builder()
                .comment(ResComment.from(comment))
                .user(ResUser.from(comment.getUser()))
                .build();
    }
}
