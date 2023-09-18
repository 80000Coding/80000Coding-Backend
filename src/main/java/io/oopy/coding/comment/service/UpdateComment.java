package io.oopy.coding.comment.service;

import io.oopy.coding.comment.dto.UpdateCommentDTO;
import io.oopy.coding.domain.comment.entity.Comment;
import io.oopy.coding.domain.comment.repository.CommentRepository;
import io.oopy.coding.domain.entity.User;
import io.oopy.coding.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateComment {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public UpdateCommentDTO.Res updateComment(UpdateCommentDTO.Req request) {

        Comment comment = commentRepository.findById(request.getComment_id()).orElse(null);

        if (comment.getDeleteAt() != null) {
            UpdateCommentDTO.Res.DeletedComment failureData = UpdateCommentDTO.Res.DeletedComment.builder()
                    .comment_id(request.getComment_id())
                    .deleted_at(comment.getDeleteAt())
                    .build();

            return UpdateCommentDTO.Res.builder()
                    .status("fail")
                    .data(failureData)
                    .message("Deleted Comment")
                    .build();
        }

        User user = comment.getUser();

        if (user == null) {
            return UpdateCommentDTO.Res.builder()
                    .status("fail")
                    .data(null)
                    .message("User does not exist")
                    .build();
        } else if (comment.getUser().getId() != request.getUser_id()) {
            return UpdateCommentDTO.Res.builder()
                    .status("fail")
                    .data(null)
                    .message("User does not have Authorization")
                    .build();
        }

        int count = commentRepository.updateCommentBody(request.getComment_id(), request.getContent());

        Comment updated = commentRepository.findById(request.getComment_id()).orElse(null);

        UpdateCommentDTO.Res.Data data = UpdateCommentDTO.Res.Data.builder()
                .comment_id(updated.getId())
                .created_at(updated.getCreatedAt())
                .updated_at(updated.getUpdatedAt())
                .deleted_at(updated.getDeleteAt())
                .build();


        UpdateCommentDTO.Res result;

        if (count == 0) {
            result = UpdateCommentDTO.Res.builder()
                    .status("fail")
                    .message("fail to update CommentBody")
                    .build();
        } else {
            result = UpdateCommentDTO.Res.builder()
                    .status("success")
                    .data(data)
                    .message("success to update CommentBody")
                    .build();
        }

        return result;
    }
}
