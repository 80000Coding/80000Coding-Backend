package io.oopy.coding.comment.service;

import io.oopy.coding.comment.dto.CommentDTO;
import io.oopy.coding.comment.dto.CreateCommentDTO;
import io.oopy.coding.domain.comment.entity.Comment;
import io.oopy.coding.domain.comment.repository.CommentRepository;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.repository.ContentRepository;
import io.oopy.coding.domain.entity.User;
import io.oopy.coding.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateComment {
    private final ContentRepository contentRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    public CreateCommentDTO.Res createComment(CreateCommentDTO.Req request) {

        Content content = contentRepository.findById(request.getContent_id()).orElse(null);
        if (content == null) {
            return CreateCommentDTO.Res.builder()
                    .status("fail")
                    .message("Content does not exist")
                    .build();
        } else if (content.getDeleteAt() != null) {
            CreateCommentDTO.Res.DeletedContent failureData = CreateCommentDTO.Res.DeletedContent.builder()
                    .content_id(request.getContent_id())
                    .deleted_at(content.getDeleteAt())
                    .build();

            return CreateCommentDTO.Res.builder()
                    .status("fail")
                    .data(failureData)
                    .message("Deleted Content")
                    .build();
        }

        User user = userRepository.findById(request.getUser_id()).orElse(null);
        if (user == null) {
            return CreateCommentDTO.Res.builder()
                    .status("fail")
                    .data(null)
                    .message("User does not exist")
                    .build();
        }

        if (request.getParent_id() != null) {
            Comment parentComment = commentRepository.findById(request.getParent_id()).orElse(null);
            if (parentComment.getDeleteAt() != null) {
                return CreateCommentDTO.Res.builder()
                        .status("fail")
                        .data(null)
                        .message("ParentComment does not exist")
                        .build();
            }
        }

        Comment newComment = Comment.builder()
                .content(content)
                .user(user)
                .parentId(request.getParent_id() != null ? request.getParent_id() : null)
                .commentBody(request.getContent())
                .deleteAt(null)
                .build();

        Comment save = commentRepository.save(newComment);

        if (save != null) {
            CreateCommentDTO.Res.successResponse successResponse = CreateCommentDTO.Res.successResponse.builder()
                    .comment_id(newComment.getId())
                    .content_id(request.getContent_id())
                    .comment_body(newComment.getCommentBody())
                    .parent_id(newComment.getParentId())
                    .created_at(newComment.getCreatedAt())
                    .updated_at(newComment.getUpdatedAt())
                    .deleted_at(newComment.getDeleteAt())
                    .build();

            return CreateCommentDTO.Res.builder()
                    .status("success")
                    .message("Comment created")
                    .data(successResponse)
                    .build();
        } else {
            return CreateCommentDTO.Res.builder()
                    .status("fail")
                    .message("Create Comment fail")
                    .build();
        }
    }


}