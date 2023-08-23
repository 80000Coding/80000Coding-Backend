package io.oopy.coding.comment.service;

import io.oopy.coding.domain.comment.dto.CreateCommentDTO;
import io.oopy.coding.domain.comment.entity.Comment;
import io.oopy.coding.domain.comment.repository.CommentRepository;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.repository.ContentRepository;
import io.oopy.coding.domain.entity.User;
import io.oopy.coding.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateComment {
    private final ContentRepository contentRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    public CreateCommentDTO createComment(Long contentId, String commentBody, Long parentId) {
        Content content = contentRepository.findById(contentId).orElseThrow(() -> {
            return new EntityNotFoundException("Content " + contentId + " not found.");
        });

        User user = userRepository.findById(parentId).orElse(null);

        Comment newComment = Comment.builder()
                .content(content)
                .user(user)
                .parentId(parentId)
                .commentBody(commentBody)
                .deleteAt(null)
                .build();

        Comment save;
        try {
            save = commentRepository.save(newComment);
        } catch (Exception e) {
            save = null;
        }

        CreateCommentDTO result;
        if (save != null) {
            result = CreateCommentDTO.builder()
                    .code("success")
                    .build();
        } else {
            result = CreateCommentDTO.builder()
                    .code("fail")
                    .build();
        }

        return result;
    }
}
