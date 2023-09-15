package io.oopy.coding.content.service;

import io.oopy.coding.content.dto.DeleteContentDTO;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteContent {
    private final ContentRepository contentRepository;

    public DeleteContentDTO.Res deleteContent(DeleteContentDTO.Req request) {

        Content content = contentRepository.findById(request.getContentId()).orElse(null);

        if (content == null) {
            DeleteContentDTO.Res.ContentEmpty failureData = DeleteContentDTO.Res.ContentEmpty.builder()
                    .content_id(request.getContentId())
                    .build();

            return DeleteContentDTO.Res.builder()
                    .status("fail")
                    .data(failureData)
                    .message("Content does not exist")
                    .build();
        } else if (content.getDeleteAt() != null) {
            return DeleteContentDTO.Res.builder()
                    .status("fail")
                    .data(null)
                    .message("Already Deleted")
                    .build();
        }

        softDeleteContent(request.getContentId());

        Content deleted = contentRepository.findById(request.getContentId()).orElse(null);

        DeleteContentDTO.Res.Data data = DeleteContentDTO.Res.Data.builder()
                .content_id(deleted.getId())
                .created_at(deleted.getCreatedAt())
                .updated_at(deleted.getUpdatedAt())
                .deleted_at(deleted.getDeleteAt())
                .build();

        return DeleteContentDTO.Res.builder()
                .status("success")
                .data(data)
                .message("Delete success")
                .build();
    }

    public int softDeleteContent(Long contentId) {
        return contentRepository.softDeleteById(contentId);
    }

}
