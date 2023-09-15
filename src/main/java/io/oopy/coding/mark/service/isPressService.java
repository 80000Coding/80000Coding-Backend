package io.oopy.coding.mark.service;

import io.oopy.coding.mark.dto.IsPressDTO;
import io.oopy.coding.domain.mark.entity.ContentMark;
import io.oopy.coding.domain.mark.repository.ContentMarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class isPressService {
    private final ContentMarkRepository contentMarkRepository;

    public IsPressDTO isPress(Long contentId, Long userId) {
        long like = 0;
        long bookmark = 0;

        List<ContentMark> pressMarks = contentMarkRepository.findContentMarkPressByContentIdAndUserId(contentId, userId);
        for (ContentMark pressMark : pressMarks) {
            if (pressMark.getType() == "like") {
                like = 1;
            }
            else if (pressMark.getType() == "bookmark") {
                bookmark = 1;
            }
        }

        return IsPressDTO.builder()
                .like(like)
                .bookmark(bookmark)
                .build();
    }
}
