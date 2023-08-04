package io.oopy.coding.domain.content.service;

import io.oopy.coding.domain.content.dao.CategoryRepository;
import io.oopy.coding.domain.content.dao.ContentCategoryRepository;
import io.oopy.coding.domain.content.dao.ContentRepository;
import io.oopy.coding.domain.content.dto.ContentDetailDTO;
import io.oopy.coding.domain.content.entity.Category;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.entity.ContentCategory;
import io.oopy.coding.domain.entity.User;
import io.oopy.coding.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentService {
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final ContentCategoryRepository contentCategoryRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ContentService(ContentRepository contentRepository, UserRepository userRepository, ContentCategoryRepository contentCategoryRepository, ContentCategoryRepository contentCategoryRepository1, CategoryRepository categoryRepository) {
        this.contentRepository = contentRepository;
        this.userRepository = userRepository;
        this.contentCategoryRepository = contentCategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    public ContentDetailDTO getContentDetails(Long contentId) {
        Content content = contentRepository.findById(contentId).orElse(null);
        if (content == null) {
            return null;
        }
        User user = userRepository.findById(content.getUser().getId()).orElse(null);
        if (user == null) {
            return null;
        }
        ContentCategory contentCategory = contentCategoryRepository.findByContentId(content.getId());
        if (contentCategory == null) {
            return null;
        }
        Category category = categoryRepository.findById(contentCategory.getId()).orElse(null);
        if (category == null) {
            return null;
        }
        return convertContentDetailDTO(content, user, contentCategory, category);
    }

    public ContentDetailDTO convertContentDetailDTO(Content content, User user, ContentCategory contentCategory, Category category) {
        ContentDetailDTO contentDetailDTO = new ContentDetailDTO();

        contentDetailDTO.setContentId(content.getId());
        contentDetailDTO.setContentBody(content.getBody());
        contentDetailDTO.setContentTitle(content.getTitle());
        contentDetailDTO.setContentType(content.getType());
        contentDetailDTO.setUserName(user.getName());
        contentDetailDTO.setUserProfileImageUrl(user.getProfileImageUrl());
        contentDetailDTO.setContentCategoryCategoryId(contentCategory.getId());
        contentDetailDTO.setCategoryColor(category.getColor());
        contentDetailDTO.setCategoryName(category.getName());

        return contentDetailDTO;
    }

}
