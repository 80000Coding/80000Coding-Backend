package io.oopy.coding.content.service;

import io.oopy.coding.content.dto.UpdateContentDTO;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateContent {
    private final ContentRepository contentRepository;

    @Transactional
    public UpdateContentDTO.Res updateContent(UpdateContentDTO.Req request) {
        Content content = contentRepository.findById(request.getContent_id()).orElse(null);

        if (content == null) {
            UpdateContentDTO.Res.ContentEmpty failureData = UpdateContentDTO.Res.ContentEmpty.builder()
                    .content_id(request.getContent_id())
                    .build();

            return UpdateContentDTO.Res.builder()
                    .status("fail")
                    .data(failureData)
                    .message("Content does not exist")
                    .build();
        } else if (content.getDeleteAt() != null) {
            UpdateContentDTO.Res.DeletedContent failureData = UpdateContentDTO.Res.DeletedContent.builder()
                    .content_id(request.getContent_id())
                    .deleted_at(content.getDeleteAt())
                    .build();

            return UpdateContentDTO.Res.builder()
                    .status("fail")
                    .data(failureData)
                    .message("Deleted Content")
                    .build();
        }

        int count = contentRepository.updateContentById(request.getContent_id(), request.getTitle(), request.getBody());

        Content updated = contentRepository.findById(request.getContent_id()).orElse(null);

        UpdateContentDTO.Res.Data data = UpdateContentDTO.Res.Data.builder()
                .content_id(updated.getId())
                .created_at(updated.getCreatedAt())
                .updated_at(updated.getUpdatedAt())
                .deleted_at(updated.getDeleteAt())
                .build();

        if (count == 0) {
            return UpdateContentDTO.Res.builder()
                    .status("fail")
                    .message("Content update fail.")
                    .build();
        } else {
            return UpdateContentDTO.Res.builder()
                    .status("success")
                    .data(data)
                    .message("Content update success.")
                    .build();
        }
    }
}
