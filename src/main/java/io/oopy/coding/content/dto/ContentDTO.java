package io.oopy.coding.content.dto;

import io.oopy.coding.domain.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContentDTO {
    private Long id;
    private User user;
    private String type;
    private String title;
    private String body;
    private String repoName;
    private String repoOwner;
    private Long views;
    private LocalDateTime deleteAt;
}