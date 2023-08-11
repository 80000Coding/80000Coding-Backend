package io.oopy.coding.domain.comment.dto;

import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
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
