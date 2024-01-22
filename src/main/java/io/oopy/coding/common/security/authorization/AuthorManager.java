package io.oopy.coding.common.security.authorization;

import io.oopy.coding.common.security.authentication.CustomUserDetails;
import io.oopy.coding.domain.comment.repository.CommentRepository;
import io.oopy.coding.domain.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorManager {

    private final ContentRepository contentRepository;
    private final CommentRepository commentRepository;

    public boolean isContentAuthor(CustomUserDetails userDetails, Long contentId) {
        return contentRepository.existsByIdAndUserId(contentId, userDetails.getUserId());
    }

    public boolean isCommentAuthor(CustomUserDetails userDetails, Long commentId) {
        return commentRepository.existsByIdAndUserId(commentId, userDetails.getUserId());
    }
}
