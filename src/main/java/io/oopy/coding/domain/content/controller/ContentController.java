package io.oopy.coding.domain.content.controller;

import io.oopy.coding.domain.content.dto.ContentDetailDTO;
import io.oopy.coding.domain.content.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContentController {
    private final ContentService contentService;

    @Autowired
    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping("/content")
    public ResponseEntity<ContentDetailDTO> getContentDetails(@RequestParam("id") Long contentId) {
        ContentDetailDTO contentDetailDTO = contentService.getContentDetails(contentId);

        if(contentDetailDTO == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(contentDetailDTO);
    }
}
