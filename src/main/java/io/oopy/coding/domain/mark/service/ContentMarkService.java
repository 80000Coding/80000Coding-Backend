package io.oopy.coding.domain.mark.service;

import io.oopy.coding.domain.content.dao.ContentRepository;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.entity.ContentCategory;
import io.oopy.coding.domain.mark.dao.ContentMarkRepository;
import io.oopy.coding.domain.mark.entity.ContentMark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ContentMarkService {
    private final ContentMarkRepository contentMarkRepository;

    @Autowired
    public ContentMarkService(ContentMarkRepository contentMarkRepository) {
        this.contentMarkRepository = contentMarkRepository;
    }

    public Map<String, Integer> getCountByType(Long contentId) {
        Content content = contentMarkRepository.findById(contentId);
        ContentMark contentMark = contentMarkRepository.findById(content.get);
        if (contentMark == null) {
            // 처리
            return null;
        }

        Map<String, Integer> countByType = new HashMap<>();
        int likeCount = 0;
        int bookmarkCount = 0;

        if ("like".equals(contentMark.getType())) {
            likeCount++;
        } else if ("bookmark".equals(contentMark.getType())) {
            bookmarkCount++;
        }

        countByType.put("like", likeCount);
        countByType.put("bookmark", bookmarkCount);

        return countByType;
    }
}
