package io.oopy.coding.domain.comment.service;

import io.oopy.coding.domain.comment.dao.CommentRepository;
import io.oopy.coding.domain.comment.dto.GetCommentDTO;
import io.oopy.coding.domain.comment.entity.Comment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<GetCommentDTO> getComments(Long contentId) {
        List<GetCommentDTO> getCommentDTO = new ArrayList<>();
        List<Comment> comments = commentRepository.findCommentsByContentId(contentId);

        for (Comment comment : comments) {
            getCommentDTO.add(convertGetCommemtDTO(comment));
        }
        return getCommentDTO;
    }

    public GetCommentDTO convertGetCommemtDTO(Comment comment) {
        GetCommentDTO getCommentDTO = new GetCommentDTO();

        getCommentDTO.setCommentBody(comment.getCommentBody());
        getCommentDTO.setCommentCreatedAt(LocalDateTime.now());
        getCommentDTO.setUserName(comment.getUser().getName());
        getCommentDTO.setUserProfileImageUrl(comment.getUser().getProfileImageUrl());

        return getCommentDTO;
    }


}
