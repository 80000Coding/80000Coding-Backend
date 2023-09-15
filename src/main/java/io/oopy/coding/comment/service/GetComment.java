package io.oopy.coding.comment.service;

import io.oopy.coding.domain.comment.repository.CommentRepository;
import io.oopy.coding.comment.dto.GetCommentDTO;
import io.oopy.coding.domain.comment.entity.Comment;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetComment {
    private final ContentRepository contentRepository;
    private final CommentRepository commentRepository;

    public GetCommentDTO.Res getComments(GetCommentDTO.Req request) {
        Content content = contentRepository.findById(request.getContentId()).orElse(null);

        if (content == null) {
            GetCommentDTO.Res.ContentEmpty failureData = GetCommentDTO.Res.ContentEmpty.builder()
                    .content_id(request.getContentId())
                    .build();

            return GetCommentDTO.Res.builder()
                    .status("fail")
                    .data(failureData)
                    .message("Content does not exist")
                    .build();
        }

        if (content.getDeleteAt() != null) {
            GetCommentDTO.Res.DeletedContent failureData = GetCommentDTO.Res.DeletedContent.builder()
                    .content_id(request.getContentId())
                    .deleted_at(content.getDeleteAt())
                    .build();

            return GetCommentDTO.Res.builder()
                    .status("fail")
                    .data(failureData)
                    .message("Deleted Content")
                    .build();
        }

        List<GetCommentDTO.Res.Data> result = new ArrayList<>();
        List<Comment> comments = commentRepository.findCommentsByContentId(request.getContentId());
        if (comments.isEmpty()) {
            return GetCommentDTO.Res.builder()
                    .status("success")
                    .data(null)
                    .message("Content has not comment")
                    .build();
        }

        for (Comment comment : comments) {
            result.add(getResponse(comment));
        }
        return GetCommentDTO.Res.builder()
                .status("success")
                .data(result)
                .build();
    }

    public GetCommentDTO.Res.Data getResponse(Comment comment) {
        GetCommentDTO.Res.Comment resComment = GetCommentDTO.Res.Comment.builder()
                .body(comment.getCommentBody())
                .parent_id(comment.getParentId())
                .created_at(comment.getCreatedAt())
                .updated_at(comment.getUpdatedAt())
                .deleted_at(comment.getDeleteAt())
                .build();

        GetCommentDTO.Res.User resUser = GetCommentDTO.Res.User.builder()
                .name(comment.getUser().getName())
                .profile_image_url(comment.getUser().getProfileImageUrl())
                .build();

        GetCommentDTO.Res.Data resData = GetCommentDTO.Res.Data.builder()
                .comment(resComment)
                .user(resUser)
                .build();

        return resData;
    }


}
