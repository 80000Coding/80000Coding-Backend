package io.oopy.coding.comment.service;

import io.oopy.coding.domain.comment.dto.UpdateCommentDTO;
import io.oopy.coding.domain.comment.entity.Comment;
import io.oopy.coding.domain.comment.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateComment {

    private final CommentRepository commentRepository;

    public UpdateCommentDTO updateComment(Long commentId, String commentBody) {
        int count = commentRepository.updateCommentBody(commentId, commentBody);

        UpdateCommentDTO result;

        if (count == 0) {
            result = UpdateCommentDTO.builder()
                    .code("fail to update CommentBody")
                    .build();
        } else {
            result = UpdateCommentDTO.builder()
                    .code("success to update CommentBody")
                    .build();
        }

        return result;
    }
}
