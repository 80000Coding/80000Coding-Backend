package io.oopy.coding.api.mark.service;

import io.oopy.coding.domain.mark.repository.ContentMarkRepository;
import io.oopy.coding.mark.dto.CountMarkDTO;
import io.oopy.coding.domain.mark.entity.ContentMark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountMarksService {
    private final ContentMarkRepository contentMarkRepository;

    public CountMarkDTO countMarks(Long contentId) {
        long likeCount = 0;
        long bookmarkCount = 0;

        List<ContentMark> contentMarks = contentMarkRepository.findContentMarksByContentId(contentId);
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
