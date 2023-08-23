package io.oopy.coding.content.service;

import io.oopy.coding.domain.content.repository.ContentRepository;
import io.oopy.coding.domain.content.dto.ContentDetailDTO;
import io.oopy.coding.domain.content.entity.Category;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.entity.ContentCategory;
import io.oopy.coding.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class getContentDetail {
    private final ContentRepository contentRepository;

    public ContentDetailDTO getContentDetails(Long contentId) {
        Content content = contentRepository.findById(contentId).orElse(null);
        User user = contentRepository.findUserByUserId(contentId);
        Category category = contentRepository.findCategoryById(contentId);
        ContentCategory contentCategory = contentRepository.findContentCategoryById(contentId);

        return ContentDetailDTO.builder()
                .contentId(content.getId())
                .contentBody(content.getBody())
                .contentTitle(content.getTitle())
                .contentType(content.getType())
                .userName(user.getName())
                .userProfileImageUrl(user.getProfileImageUrl())
                .contentCategoryCategoryId(contentCategory.getId())
                .categoryColor(category.getColor())
                .categoryName(category.getName())
                .build();
    }
}
