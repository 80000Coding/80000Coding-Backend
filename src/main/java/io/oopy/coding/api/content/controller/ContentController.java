package io.oopy.coding.api.content.controller;

import io.oopy.coding.domain.content.dto.ContentDetailDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/content")
public class ContentController {
    private final io.oopy.coding.api.content.service.getContentDetailService getContentDetailService;

    @Operation(summary = "게시판 상세 페이지", description = "게시판 상세 페이지")
    @GetMapping("")
    public ContentDetailDTO contentDetailDTO() {
        return ContentDetailDTO.builder()
                .contentId(1L)
                .contentTitle("test title")
                .contentBody("test body")
                .contentType("post")
                .userName("test user")
                .userProfileImageUrl("test.com")
                .contentCategoryCategoryId(2L)
                .categoryColor("red")
                .categoryName("test category")
                .build();
    }
}
