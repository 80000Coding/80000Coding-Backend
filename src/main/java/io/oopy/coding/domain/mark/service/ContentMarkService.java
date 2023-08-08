package io.oopy.coding.domain.mark.service;

import io.oopy.coding.domain.content.dao.ContentRepository;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.mark.dao.ContentMarkRepository;
import io.oopy.coding.domain.mark.dto.CountMarkDTO;
import io.oopy.coding.domain.mark.entity.ContentMark;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentMarkService {
    private final ContentRepository contentRepository;
    private final ContentMarkRepository contentMarkRepository;

    public ContentMarkService(ContentRepository contentRepository, ContentMarkRepository contentMarkRepository) {
        this.contentRepository = contentRepository;
        this.contentMarkRepository = contentMarkRepository;
    }

    public CountMarkDTO countMarks(Long contentId) {
        long likeCount = 0;
        long bookmarkCount = 0;

        Content content = contentRepository.findById(contentId).orElse(null);

        List<ContentMark> contentMarks = contentMarkRepository.findContentMarksByContentId(content.getId());
        for (ContentMark contentMark : contentMarks) {
            if (contentMark.getType() == "like") {
                likeCount++;
            }
            else if (contentMark.getType() == "bookmark") {
                bookmarkCount++;
            }
        }

        return CountMarkDTO.builder()
                .like(likeCount)
                .bookmark(bookmarkCount)
                .build();
    }
}
