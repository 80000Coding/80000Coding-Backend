package io.oopy.coding.api.content.service;

import io.oopy.coding.api.user.service.UserSearchService;
import io.oopy.coding.domain.comment.repository.CommentRepository;
import io.oopy.coding.domain.content.entity.ContentType;
import io.oopy.coding.api.content.exception.ContentErrorCode;
import io.oopy.coding.api.content.exception.ContentErrorException;
import io.oopy.coding.common.security.authentication.CustomUserDetails;
import io.oopy.coding.domain.content.dto.*;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.repository.ContentRepository;
import io.oopy.coding.domain.user.entity.RoleType;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentService {
    private final ContentRepository contentRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final UserSearchService userSearchService;

    /**
     * 게시글 상세 페이지
     * @param contentId
     */
    @Transactional
    public GetContentRes getContent(Long contentId) {
        Content content = findContent(contentId);

        Long commentCount = commentRepository.countByContentId(contentId);
        if (content.getDeleteAt() != null)
            throw new ContentErrorException(ContentErrorCode.DELETED_CONTENT);

        content.plusViewCount();

        return GetContentRes.from(content, commentCount);
    }

    /**
     * 게시글 생성
     * @param req
     * @return contentId
     */
    public CreateContentRes createContent(CreateContentReq req, CustomUserDetails securityUser) {

        User user = userRepository.findById(securityUser.getUserId()).orElse(null);

        checkValidateType(req.getType(), req.getRepoName(), req.getRepoOwner());

        Content newContent = Content.builder()
                .user(user)
                .contentCategories(Collections.emptyList())
                .contentMarks(Collections.emptyList())
                .comments(Collections.emptyList())
                .type(ContentType.fromString(req.getType()))
                .title("")
                .body("")
                .repoOwner(req.getRepoOwner() != null ? req.getRepoOwner() : null)
                .repoName(req.getRepoName() != null ? req.getRepoName() : null)
                .views(0L)
                .publish(false)
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
    public UpdateContentRes updateContent(Long contentId, UpdateContentReq req, CustomUserDetails securityUser) {

        Content content = findContent(contentId);

        if(content.getDeleteAt() != null)
            throw new ContentErrorException(ContentErrorCode.DELETED_CONTENT);

        contentRepository.save(content.update(req.getTitle(), req.getBody(), req.getContentImageUrl(), req.getPublish()));

        return UpdateContentRes.of(content.getId(), content.getUpdatedAt());
    }

    /**
     * 게시글 삭제 (soft delete)
     * @param contentId
     * @return contentId, deletedAt
     */
    @Transactional
    public DeleteContentRes deleteContent(Long contentId, CustomUserDetails securityUser) {
        Content content = findContent(contentId);

        if(content.getDeleteAt() != null)
            throw new ContentErrorException(ContentErrorCode.DELETED_CONTENT);

        content.softDelete();

        return DeleteContentRes.of(content.getId(), content.getDeleteAt());
    }

    /**
     * 유저 개인 게시글 검색(현재 개수 제한X, 페이징 기능 필요할 것 같은데 아직 넣지 않음)
     * @param securityUser
     * @return
     */
    public List<UserContentRes> getUserContents(CustomUserDetails securityUser) {
        User user = userSearchService.findById(securityUser.getUserId());

        List<Content> contents = contentRepository.findContentsByUserIdAndPublishIsTrue(user.getId());

        List<UserContentRes> response = new ArrayList<>();

        for (Content content : contents) {
            String resBody = processBodyData(content.getBody());
            UserContentRes dto = UserContentRes.from(content, resBody);
            response.add(dto);
        }

        return response;
    }

    public Content findContent(Long contentId) {
        return contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentErrorException(ContentErrorCode.INVALID_CONTENT_ID));
    }



    /**
     * 타입에 따른 valid 확인
     */
    private void checkValidateType(String type, String repoName, String repoOwner) {
        if (type.equals("post")) {
            if (repoName != null || repoOwner != null) {
                throw new ContentErrorException(ContentErrorCode.MISS_MATCH_POST_TYPE);
            }
        } else if (type.equals("project")) {
            if (repoName == null || repoOwner == null) {
                throw new ContentErrorException(ContentErrorCode.MISS_MATCH_PROJECT_TYPE);
            }
        } else {
            throw new ContentErrorException(ContentErrorCode.INVALID_CONTENT_TYPE);
        }
    }

    private String processBodyData(String body) {
        String singleLine = StringUtils.replace(body, "\n", " ");

        String trimmed = singleLine.stripLeading();
        String limited = trimmed.substring(0, Math.min(trimmed.length(), 150));

        if (limited.length() == 150) {
            limited = limited.substring(0, limited.length() - 3) + "...";
        }

        return limited.trim();
    }


}
