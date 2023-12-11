package io.oopy.coding.api.mark.service;

import io.oopy.coding.api.content.exception.ContentErrorCode;
import io.oopy.coding.api.content.exception.ContentErrorException;
import io.oopy.coding.api.mark.MarkType;
import io.oopy.coding.domain.mark.dto.ChangeUserPressReq;
import io.oopy.coding.domain.mark.dto.ContentMarkDto;
import io.oopy.coding.common.security.authentication.CustomUserDetails;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.repository.ContentRepository;
import io.oopy.coding.domain.mark.dto.UserPressReq;
import io.oopy.coding.domain.mark.entity.ContentMark;
import io.oopy.coding.domain.mark.repository.ContentMarkRepository;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentMarkService {
    private final ContentRepository contentRepository;
    private final ContentMarkRepository contentMarkRepository;
    private final UserRepository userRepository;

    public ContentMarkDto getMarkByContent(Long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentErrorException(ContentErrorCode.INVALID_CONTENT_ID));

        List<ContentMark> marks = contentMarkRepository.findContentMarksByContentId(contentId);

        Long likeCount = 0L;
        Long bookMarkCount = 0L;

        for (ContentMark contentMark : marks) {
            if (contentMark.getType().equals(MarkType.LIKE))
                likeCount++;
            else if (contentMark.getType().equals(MarkType.BOOKMARK))
                bookMarkCount++;
//            else -> type이 다를 경우 예외
        }

        ContentMarkDto response = ContentMarkDto.builder()
                .like(likeCount)
                .bookmark(bookMarkCount)
                .build();

        return response;
    }

    @Transactional
    public UserPressReq getUserPress(CustomUserDetails securityUser, Long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentErrorException(ContentErrorCode.INVALID_CONTENT_ID));

        List<ContentMark> marks = contentMarkRepository.findContentMarksByContentIdAndUserId(contentId, securityUser.getUserId());
        
        boolean like = false;
        boolean bookmark = false;

        if (marks != null) {
            for (ContentMark contentMark : marks) {
                if (contentMark.getType().equals(MarkType.LIKE))
                    like = true;
                else if (contentMark.getType().equals(MarkType.BOOKMARK))
                    bookmark = true;
            }
        }

        return UserPressReq.builder()
                .like(like)
                .bookmark(bookmark)
                .build();
    }

    @Transactional
    public void changeUserPress(CustomUserDetails securityUser, ChangeUserPressReq req) {
        Content content = contentRepository.findById(req.getContentId())
                .orElseThrow(() -> new ContentErrorException(ContentErrorCode.INVALID_CONTENT_ID));

        MarkType markType = MarkType.fromType(req.getType());

        ContentMark userMark = contentMarkRepository.findContentMarksByContentIdAndUserIdAndType(req.getContentId(), securityUser.getUserId(), markType);

        if (userMark == null) {
            ContentMark mark = ContentMark.builder()
                    .user(userRepository.getReferenceById(securityUser.getUserId()))
                    .content(content)
                    .type(markType)
                    .build();

            contentMarkRepository.save(mark);
        } else {
            contentMarkRepository.delete(userMark);
        }
    }
}