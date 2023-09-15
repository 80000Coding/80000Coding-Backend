package io.oopy.coding.content.service;

import io.oopy.coding.content.dto.CreateContentDTO;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.repository.ContentRepository;
import io.oopy.coding.domain.entity.User;
import io.oopy.coding.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CreateContent {

    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

    public CreateContentDTO.Res createContent(CreateContentDTO.Req request) {

        User user = userRepository.findById(request.getUser_id()).orElse(null);

        if (user == null) {
            CreateContentDTO.Res.UserEmpty failureData = CreateContentDTO.Res.UserEmpty.builder()
                    .user_id(request.getUser_id())
                    .build();

            return CreateContentDTO.Res.builder()
                    .status("fail")
                    .data(failureData)
                    .message("User does not exist")
                    .build();
        }

        Content newContent = Content.builder()
                .user(user)
                .contentCategories(Collections.emptyList())
                .contentMarks(Collections.emptyList())
                .comments(Collections.emptyList())
                .type(request.getType())
                .title("")
                .body("")
                .repoOwner(request.getRepo_owner() != null ? request.getRepo_owner() : null)
                .repoName(request.getRepo_name() != null ? request.getRepo_name() : null)
                .views(0L)
                .complete(false)
                .contentImageUrl("")
                .deleteAt(null)
                .build();

        Content save = contentRepository.save(newContent);

        CreateContentDTO.Res result;

        if (save != null) {
            CreateContentDTO.Res.Data responseData = CreateContentDTO.Res.Data.builder()
                    .content_id(save.getId())
                    .created_at(save.getCreatedAt())
                    .updated_at(save.getUpdatedAt())
                    .deleted_at(save.getDeleteAt())
                    .build();
            result = CreateContentDTO.Res.builder()
                    .status("success")
                    .data(responseData)
                    .build();
        } else {
            result = CreateContentDTO.Res.builder()
                    .status("fail")
                    .message("create Content failed")
                    .build();
        }

        return result;
    }
}
