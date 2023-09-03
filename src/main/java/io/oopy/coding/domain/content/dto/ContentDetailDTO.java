package io.oopy.coding.domain.content.dto;

import io.oopy.coding.domain.content.entity.ContentCategory;
import io.oopy.coding.domain.user.entity.User;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContentDetailDTO {
    private Long contentId;
    private String contentTitle;
    private String contentBody;
    private String contentType;
    private String userName;
    private String userProfileImageUrl;
    private Long contentCategoryCategoryId;
    private String categoryColor;
    private String categoryName;
}
