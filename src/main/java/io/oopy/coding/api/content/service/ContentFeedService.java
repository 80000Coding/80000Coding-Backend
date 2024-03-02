package io.oopy.coding.api.content.service;

import io.oopy.coding.domain.content.dto.ContentDTO;
import io.oopy.coding.domain.content.dto.ContentDetailDTO;
import io.oopy.coding.domain.content.dto.ContentFeedSearchReq;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.repository.ContentQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContentFeedService {

    private final ContentQueryRepository contentQueryRepository;

    @Transactional(readOnly = true)
    public Page<ContentDTO> searchByTitle(ContentFeedSearchReq.Title search, Pageable pageable) {
        Page<Content> contents = contentQueryRepository.findByTitle(search.getTitle(), pageable);
        return getContentDTO(contents);
    }

    @Transactional(readOnly = true)
    public Page<ContentDTO> searchByBody(ContentFeedSearchReq.Body search, Pageable pageable) {
        Page<Content> contents = contentQueryRepository.findByBody(search.getBody(), pageable);
        return getContentDTO(contents);
    }

    // private //
    public Page<ContentDTO> getContentDTO(Page<Content> contents) {
        return contents.map(v-> {
            ContentDTO contentDTO = ContentDTO.toDTO(v);
            return contentDTO;
        });
    }

    @Transactional(readOnly = true)
    public Page<ContentDetailDTO> searchPostByTitle(ContentFeedSearchReq.Title search, Pageable pageable) {
        return contentQueryRepository.findPostByTitle(search.getTitle(), pageable);
    }
    @Transactional(readOnly = true)
    public Page<ContentDetailDTO> searchPostByBody(ContentFeedSearchReq.Body search, Pageable pageable) {
        return contentQueryRepository.findPostByBody(search.getBody(), pageable);
    }

    @Transactional(readOnly = true)
    public Page<ContentDetailDTO> searchProjectByTitle(ContentFeedSearchReq.Title search, Pageable pageable) {
        return contentQueryRepository.findProjectByTitle(search.getTitle(), pageable);
    }
    @Transactional(readOnly = true)
    public Page<ContentDetailDTO> searchProjectByBody(ContentFeedSearchReq.Body search, Pageable pageable) {
        return contentQueryRepository.findProjectByBody(search.getBody(), pageable);
    }
}
