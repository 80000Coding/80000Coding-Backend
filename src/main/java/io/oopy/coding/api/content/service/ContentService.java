package io.oopy.coding.api.content.service;

import io.oopy.coding.api.content.exception.ContentErrorCode;
import io.oopy.coding.api.content.exception.ContentErrorException;
import io.oopy.coding.common.response.SuccessResponse;
import io.oopy.coding.domain.content.dto.*;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.repository.ContentRepository;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

    /**
     * 게시글 상세 페이지
     * @param contentId
     */
    @Transactional
    public SuccessResponse getContent(Long contentId) {

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentErrorException(ContentErrorCode.INVALID_CONTENT_ID));

        // soft Delete 된 게시글 일 경우
        if (content.getDeleteAt() != null)
            throw new ContentErrorException(ContentErrorCode.DELETED_CONTENT);

        content.plusViewCount();

        return SuccessResponse.from(GetContentRes.from(content));
    }

    /**
     * 게시글 삭제 (soft delete)
     * @param contentId
     * @return contentId, deletedAt
     */
    @Transactional
    public SuccessResponse deleteContent(Long contentId, Long userId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentErrorException(ContentErrorCode.INVALID_CONTENT_ID));

        if (!content.getUser().getId().equals(userId))
            throw new ContentErrorException(ContentErrorCode.REQUEST_USER_DATA_OWNER_MISMATCH);
        else if(content.getDeleteAt() != null)
            throw new ContentErrorException(ContentErrorCode.DELETED_CONTENT);

        content.softDelete();

        return SuccessResponse.from(DeleteContentRes.of(content.getId(), content.getDeleteAt()));
    }

    /**
     * 게시글 생성
     * @param req
     * @return contentId
     */
    public SuccessResponse createContent(CreateContentReq req, Long userId) {

        // TODO orELseThrow 부분 제거(유저 측에서 이미 검증해서 넘어올 것이기 때문)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist"));

        Content newContent = Content.builder()
                .user(user)
                .contentCategories(Collections.emptyList())
                .contentMarks(Collections.emptyList())
                .comments(Collections.emptyList())
                .type(req.getType())
                .title("")
                .body("")
                .repoOwner(req.getRepoOwner() != null ? req.getRepoOwner() : null)
                .repoName(req.getRepoName() != null ? req.getRepoName() : null)
                .views(0L)
                .complete(false)
                .contentImageUrl("")
                .deleteAt(null)
                .build();

        contentRepository.save(newContent);

        return SuccessResponse.from(CreateContentRes.of(newContent.getId()));
    }

    /**
     * 게시글 수정
     * @param req
     * @return contentId, updatedAt
     */
    public SuccessResponse updateContent(UpdateContentReq req, Long userId) {

        Content content = contentRepository.findById(req.getContentId())
                .orElseThrow(() -> new ContentErrorException(ContentErrorCode.INVALID_CONTENT_ID));

        if (!content.getUser().getId().equals(userId))
            throw new ContentErrorException(ContentErrorCode.REQUEST_USER_DATA_OWNER_MISMATCH);
        else if(content.getDeleteAt() != null)
            throw new ContentErrorException(ContentErrorCode.DELETED_CONTENT);

        contentRepository.save(content.update(req.getTitle(), req.getBody()));

        return SuccessResponse.from(UpdateContentRes.of(content.getId(), content.getUpdatedAt()));
    }
}
