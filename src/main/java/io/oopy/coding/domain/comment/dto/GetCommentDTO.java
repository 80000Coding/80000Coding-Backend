package io.oopy.coding.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCommentDTO {
    private String commentBody;
    private LocalDateTime commentCreatedAt;
    private String userName;
    private String userProfileImageUrl;
}
