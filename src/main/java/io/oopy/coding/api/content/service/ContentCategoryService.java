package io.oopy.coding.api.content.service;

import io.oopy.coding.api.content.exception.ContentErrorCode;
import io.oopy.coding.api.content.exception.ContentErrorException;
import io.oopy.coding.domain.content.dto.ChangeCategoryReq;
import io.oopy.coding.domain.content.dto.ContentCategoryDto;
import io.oopy.coding.domain.content.entity.Category;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.entity.ContentCategory;
import io.oopy.coding.domain.content.repository.CategoryRepository;
import io.oopy.coding.domain.content.repository.ContentCategoryRepository;
import io.oopy.coding.domain.content.repository.ContentRepository;
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
    private final ContentRepository contentRepository;
    private final ContentCategoryRepository contentCategoryRepository;
    private final CategoryRepository categoryRepository;

    public List<ContentCategoryDto> getCategories(Long contentId) {
        contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentErrorException(ContentErrorCode.INVALID_CONTENT_ID));

        List<ContentCategory> categories = contentCategoryRepository.findContentCategoriesByContentId(contentId);

        List<ContentCategoryDto> response = new ArrayList<>();
        if (categories.isEmpty()) {
            throw new ContentErrorException(ContentErrorCode.EMPTY_CATEGORY);
        }

        for (ContentCategory contentCategory : categories) {
            Category category = categoryRepository.findById(contentCategory.getCategory().getId())
                    .orElseThrow(() -> new ContentErrorException(ContentErrorCode.INVALID_CONTENT_CATEGORY));
            ContentCategoryDto dto = ContentCategoryDto.toDTO(category);
            response.add(dto);
        }

        return response;
    }

    public boolean addCategory(ChangeCategoryReq req) {
        Content content = contentRepository.findById(req.getContentId())
                .orElseThrow(() -> new ContentErrorException(ContentErrorCode.INVALID_CONTENT_ID));

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
    public boolean deleteCategory(ChangeCategoryReq req) {
        contentRepository.findById(req.getContentId())
                .orElseThrow(() -> new ContentErrorException(ContentErrorCode.INVALID_CONTENT_ID));

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
