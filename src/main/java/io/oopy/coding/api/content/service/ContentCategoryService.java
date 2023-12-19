package io.oopy.coding.api.content.service;

import io.oopy.coding.api.content.exception.ContentErrorCode;
import io.oopy.coding.api.content.exception.ContentErrorException;
import io.oopy.coding.common.security.authentication.CustomUserDetails;
import io.oopy.coding.domain.content.dto.ChangeCategoryReq;
import io.oopy.coding.domain.content.dto.ContentCategoryDto;
import io.oopy.coding.domain.content.entity.Category;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.entity.ContentCategory;
import io.oopy.coding.domain.content.repository.CategoryRepository;
import io.oopy.coding.domain.content.repository.ContentCategoryRepository;
import io.oopy.coding.domain.content.repository.ContentRepository;
import io.oopy.coding.domain.user.entity.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentCategoryService {
    private final ContentCategoryRepository contentCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final ContentService contentService;

    @Transactional
    public List<ContentCategoryDto> getCategories(Long contentId) {
        contentService.findContent(contentId);

        List<ContentCategory> categories = contentCategoryRepository.findContentCategoriesWithCategoryByContentId(contentId);

        if (categories.isEmpty()) {
            throw new ContentErrorException(ContentErrorCode.EMPTY_CATEGORY);
        }

        List<ContentCategoryDto> response = new ArrayList<>();

        for (ContentCategory contentCategory : categories) {
            ContentCategoryDto dto = ContentCategoryDto.toDTO(contentCategory.getCategory());
            response.add(dto);
        }

        return response;
    }

    public boolean addCategory(ChangeCategoryReq req, CustomUserDetails securityUser) {
        Content content = contentService.findContent(req.getContentId());

        if (securityUser.getRole() != RoleType.ADMIN && !content.getUser().getId().equals(securityUser.getUserId())) {
            throw new ContentErrorException(ContentErrorCode.REQUEST_USER_DATA_OWNER_MISMATCH);
        }

        Category category = categoryRepository.findByName(req.getCategory())
                .orElseThrow(() -> new ContentErrorException(ContentErrorCode.INVALID_CONTENT_CATEGORY));

        ContentCategory contentCategory = contentCategoryRepository.findContentCategoryByContentIdAndCategoryName(req.getContentId(), req.getCategory());
        if (contentCategory != null) {
            throw new ContentErrorException(ContentErrorCode.ALREADY_APPOINTED_CATEGORY);
        }

        ContentCategory newContentCategory = ContentCategory.fromContentAndCategory(content, category);

        contentCategoryRepository.save(newContentCategory);

        return true;
    }

    @Transactional
    public boolean deleteCategory(ChangeCategoryReq req, CustomUserDetails securityUser) {
        Content content = contentService.findContent(req.getContentId());

        if (securityUser.getRole() != RoleType.ADMIN && !content.getUser().getId().equals(securityUser.getUserId())) {
            throw new ContentErrorException(ContentErrorCode.REQUEST_USER_DATA_OWNER_MISMATCH);
        }

        categoryRepository.findByName(req.getCategory())
                .orElseThrow(() -> new ContentErrorException(ContentErrorCode.INVALID_CONTENT_CATEGORY));

        ContentCategory contentCategory = contentCategoryRepository.findContentCategoryByContentIdAndCategoryName(req.getContentId(), req.getCategory());
        if (contentCategory == null) {
            throw new ContentErrorException(ContentErrorCode.ALREADY_DELETED_CATEGORY);
        }

        contentCategoryRepository.delete(contentCategory);
        return true;
    }
}
