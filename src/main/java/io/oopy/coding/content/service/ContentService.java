package io.oopy.coding.content.service;

import io.oopy.coding.domain.content.dto.*;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.repository.ContentRepository;
import io.oopy.coding.domain.entity.User;
import io.oopy.coding.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public GetContentRes getContent(Long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Content does not exist"));

        content.updateViews(content.getViews() + 1);

        return getResponse(content);
    }

    private GetContentRes getResponse(Content content) {

        User user = content.getUser();
//        List<ContentCategory> contentCategory = content.getContentCategories();

        GetContentRes.Content responseContent = GetContentRes.Content.builder()
                .id(content.getId())
                .title(content.getTitle())
                .body(content.getBody())
                .type(content.getType())
                .views(content.getViews())
                .repo_name(content.getRepoName())
                .repo_owner(content.getRepoOwner())
                .complete(content.isComplete())
                .content_image_url(content.getContentImageUrl())
                .created_at(content.getCreatedAt())
                .updated_at(content.getUpdatedAt())
                .deleted_at(content.getDeleteAt())
                .build();

        GetContentRes.User responseUser = GetContentRes.User.builder()
                .name(user.getName())
                .profile_image_url(user.getProfileImageUrl())
                .build();

        GetContentRes.ContentCategory responseContentCategory = null;

        return GetContentRes.builder()
                .content(responseContent)
                .user(responseUser)
                .contentCategory(responseContentCategory)
                .build();
    }

    /**
     * 게시글 삭제 (soft delete)
     * @param contentId
     * @return contentId, deletedAt
     */
    public DeleteContentRes deleteContent(Long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("Content does not exist"));

        content.softDelete();
        contentRepository.save(content);

        return DeleteContentRes.builder()
                .content_id(contentId)
                .deleted_at(content.getDeleteAt())
                .build();
    }

    /**
     * 게시글 생성
     * @param req
     * @return contentId
     */
    public CreateContentRes createContent(CreateContentReq req) {
        User user = userRepository.findById(req.getUser_id())
                .orElseThrow(() -> new EntityNotFoundException("User does not exist"));

        Content newContent = Content.builder()
                .user(user)
                .contentCategories(Collections.emptyList())
                .contentMarks(Collections.emptyList())
                .comments(Collections.emptyList())
                .type(req.getType())
                .title("")
                .body("")
                .repoOwner(req.getRepo_owner() != null ? req.getRepo_owner() : null)
                .repoName(req.getRepo_name() != null ? req.getRepo_name() : null)
                .views(0L)
                .complete(false)
                .contentImageUrl("")
                .deleteAt(null)
                .build();

        contentRepository.save(newContent);

        return CreateContentRes.builder()
                .content_id(newContent.getId())
                .build();
    }

    /**
     * 게시글 수정
     * @param req
     * @return contentId, updatedAt
     */
    public UpdateContentRes updateContent(UpdateContentReq req) {
        Content content = contentRepository.findById(req.getContent_id())
                .orElseThrow(() -> new EntityNotFoundException("Content does not exist"));

        content.update(req.getTitle(), req.getBody());
        contentRepository.save(content);

        return UpdateContentRes.builder()
                .content_id(req.getContent_id())
                .updated_at(content.getUpdatedAt())
                .build();
    }
}
