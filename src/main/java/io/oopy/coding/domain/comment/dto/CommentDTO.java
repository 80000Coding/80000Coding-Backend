package io.oopy.coding.domain.comment.dto;

import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
    private Long id;
    private Content content;
    private User user;
    private String commentBody;
    private Long parentId;
    private LocalDateTime deleteAt;
}
