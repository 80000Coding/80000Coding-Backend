package io.oopy.coding.comment.service;

import io.oopy.coding.comment.dto.DeleteCommentDTO;
import io.oopy.coding.domain.comment.entity.Comment;
import io.oopy.coding.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteComment {

    private final CommentRepository commentRepository;

    @Transactional
    public DeleteCommentDTO.Res deleteComment(DeleteCommentDTO.Req request) {

        Comment comment = commentRepository.findById(request.getComment_id()).orElse(null);
        if (comment == null) {
            DeleteCommentDTO.Res.CommentEmpty failureData = DeleteCommentDTO.Res.CommentEmpty.builder()
                    .comment_id(request.getComment_id())
                    .build();

            return DeleteCommentDTO.Res.builder()
                    .status("error")
                    .data(failureData)
                    .message("Comment does not exist")
                    .build();
        } else if (comment.getDeleteAt() != null) {
            DeleteCommentDTO.Res.AlreadyDeleted failureData = DeleteCommentDTO.Res.AlreadyDeleted.builder()
                    .comment_id(request.getComment_id())
                    .build();

            return DeleteCommentDTO.Res.builder()
                    .status("error")
                    .data(failureData)
                    .message("Comment already deleted")
                    .build();
        }

        int count = commentRepository.softDeleteCommentById(request.getComment_id());

        Comment deleted = commentRepository.findById(request.getComment_id()).orElse(null);

        DeleteCommentDTO.Res.Data data = DeleteCommentDTO.Res.Data.builder()
                .comment_id(deleted.getId())
                .created_at(deleted.getCreatedAt())
                .updated_at(deleted.getUpdatedAt())
                .deleted_at(deleted.getDeleteAt())
                .build();

        DeleteCommentDTO.Res result;
        if (count == 0) {
            result = DeleteCommentDTO.Res.builder()
                    .status("fail")
                    .message("fail to delete Comment")
                    .data(null)
                    .build();
        } else {
            result = DeleteCommentDTO.Res.builder()
                    .status("success")
                    .message("success to delete Comment")
                    .data(data)
                    .build();
        }

        return result;
    }
}
