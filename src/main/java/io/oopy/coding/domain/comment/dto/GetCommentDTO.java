package io.oopy.coding.domain.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetCommentDTO {
    private String commentBody;
    private LocalDateTime commentCreatedAt;
    private String userName;
    private String userProfileImageUrl;
}
