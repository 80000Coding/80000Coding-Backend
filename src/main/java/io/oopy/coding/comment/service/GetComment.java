package io.oopy.coding.comment.service;

import io.oopy.coding.domain.comment.repository.CommentRepository;
import io.oopy.coding.domain.comment.dto.GetCommentDTO;
import io.oopy.coding.domain.comment.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetComment {
    private final CommentRepository commentRepository;

    public List<GetCommentDTO> getComments(Long contentId) {
        List<GetCommentDTO> getCommentDTO = new ArrayList<>();
        List<Comment> comments = commentRepository.findCommentsByContentId(contentId);

        for (Comment comment : comments) {
            getCommentDTO.add(convertGetCommemtDTO(comment));
        }
        return getCommentDTO;
    }

    public GetCommentDTO convertGetCommemtDTO(Comment comment) {
        GetCommentDTO getCommentDTO = GetCommentDTO.builder()
                .commentBody(comment.getCommentBody())
                .commentCreatedAt(LocalDateTime.now())
                .userName(comment.getUser().getName())
                .userProfileImageUrl(comment.getUser().getProfileImageUrl())
                .build();

        return getCommentDTO;
    }


}
