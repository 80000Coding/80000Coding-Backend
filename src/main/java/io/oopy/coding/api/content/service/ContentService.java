package io.oopy.coding.api.content.service;

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
    public GetContentRes getContent(Long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Content does not exist"));

        content.plusViewCount();

        return GetContentRes.from(content);
    }

    /**
     * 게시글 삭제 (soft delete)
     * @param contentId
     * @return contentId, deletedAt
     */
    @Transactional
    public DeleteContentRes deleteContent(Long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Content does not exist"));

        content.softDelete();

        return DeleteContentRes.of(content.getId(), content.getDeleteAt());
    }

    /**
     * 게시글 생성
     * @param req
     * @return contentId
     */
    public CreateContentRes createContent(CreateContentReq req) {
        User user = userRepository.findById(req.getUserId())
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

        return CreateContentRes.of(newContent.getId());
    }

    /**
     * 게시글 수정
     * @param req
     * @return contentId, updatedAt
     */
    public UpdateContentRes updateContent(UpdateContentReq req) {
        Content content = contentRepository.findById(req.getContentId())
                .orElseThrow(() -> new EntityNotFoundException("Content does not exist"));

        contentRepository.save(content.update(req.getTitle(), req.getBody()));

        return UpdateContentRes.of(content.getId(), content.getUpdatedAt());
    }
}
