package io.oopy.coding.comment.service;

import io.oopy.coding.domain.comment.dto.DeleteCommentDTO;
import io.oopy.coding.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteComment {

    private final CommentRepository commentRepository;

    public DeleteCommentDTO deleteComment(Long commentId) {
        int count;

        count = commentRepository.deleteCommentById(commentId);

        DeleteCommentDTO result;
        if (count == 0) {
            result = DeleteCommentDTO.builder()
                    .code("faile to delete Comment")
                    .build();
        } else {
            result = DeleteCommentDTO.builder()
                    .code("success to delete Comment")
                    .build();
        }

        return result;
    }
}
