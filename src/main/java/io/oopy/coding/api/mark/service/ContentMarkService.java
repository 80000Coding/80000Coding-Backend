package io.oopy.coding.api.mark.service;

import io.oopy.coding.api.content.exception.ContentErrorCode;
import io.oopy.coding.api.content.exception.ContentErrorException;
import io.oopy.coding.api.content.service.ContentService;
import io.oopy.coding.api.mark.exception.ContentMarkErrorCode;
import io.oopy.coding.api.mark.exception.ContentMarkErrorException;
import io.oopy.coding.domain.mark.entity.MarkType;
import io.oopy.coding.domain.mark.dto.ChangeUserPressReq;
import io.oopy.coding.domain.mark.dto.ContentMarkDto;
import io.oopy.coding.common.security.authentication.CustomUserDetails;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.mark.dto.CountMark;
import io.oopy.coding.domain.mark.dto.UserPressReq;
import io.oopy.coding.domain.mark.entity.ContentMark;
import io.oopy.coding.domain.mark.repository.ContentMarkRepository;
import io.oopy.coding.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentMarkService {
    private final ContentService contentService;
    private final ContentMarkRepository contentMarkRepository;
    private final UserRepository userRepository;

    public ContentMarkDto getMarkByContent(Long contentId) {
        Long likeCount = 0L;
        Long bookmarkCount = 0L;

        contentService.findContent(contentId);

        List<CountMark> counts = contentMarkRepository.getContentMarkCountsByContentId(contentId);

        for (CountMark countMark : counts) {
            likeCount = countMark.getLike();
            bookmarkCount = countMark.getBookmark();
        }

        return ContentMarkDto.builder()
                .like(likeCount == null ? 0 : likeCount)
                .bookmark(bookmarkCount == null ? 0 : bookmarkCount)
                .build();
    }

    @Transactional
    public UserPressReq getUserPress(CustomUserDetails securityUser, Long contentId) {
        contentService.findContent(contentId);

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
    public void changeUserPress(Long contentId, CustomUserDetails securityUser, ChangeUserPressReq req) {
        Content content = contentService.findContent(contentId);
        if (content.getDeleteAt() != null) {
            throw new ContentErrorException(ContentErrorCode.DELETED_CONTENT);
        }

        MarkType markType = MarkType.fromString(req.getType());
        if (markType == null) {
            throw new ContentMarkErrorException(ContentMarkErrorCode.INVALID_TYPE);
        }

        ContentMark userMark = contentMarkRepository.findContentMarksByContentIdAndUserIdAndType(contentId, securityUser.getUserId(), markType);

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