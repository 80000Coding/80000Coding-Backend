package io.oopy.coding.comment.dto;

import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommentDTO {
    private Long id;
    private Content content;
    private User user;
    private String commentBody;
    private Long parentId;
    private LocalDateTime deleteAt;
}
