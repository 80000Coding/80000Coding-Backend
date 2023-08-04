package io.oopy.coding.domain.content.dto;

import io.oopy.coding.domain.content.entity.ContentCategory;
import io.oopy.coding.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentDetailDTO {
    private Long contentId;
    private String contentTitle;
    private String contentBody;
    private String contentType;
    private String userName;
    private String userProfileImageUrl;
    private Long ContentCategoryCategoryId;
    private String CategoryColor;
    private String CategoryName;
}
