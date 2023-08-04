package io.oopy.coding.domain.content.dto;

import io.oopy.coding.domain.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentDTO {
    private Long Id;
    private User user;
    private String type;
    private String title;
    private String body;
    private String repoName;
    private String repoOwner;
    private Long views;
    private LocalDateTime deleteAt;
}

