package io.oopy.coding.content.controller;

import io.oopy.coding.content.dto.CreateContentDTO;
import io.oopy.coding.content.dto.DeleteContentDTO;
import io.oopy.coding.content.dto.GetContentDTO;
import io.oopy.coding.content.dto.UpdateContentDTO;
import io.oopy.coding.content.service.CreateContent;
import io.oopy.coding.content.service.DeleteContent;
import io.oopy.coding.content.service.GetContent;
import io.oopy.coding.content.service.UpdateContent;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/content")
public class ContentController {

    private final GetContent getContentService;
    private final CreateContent createContentService;
    private final UpdateContent updateContentService;
    private final DeleteContent deleteContentService;


    @Operation(summary = "게시글 상세 페이지", description = "Request로 content_id")
    @GetMapping("")
    public GetContentDTO.Res GetContent(@RequestParam Long contentId) {

        GetContentDTO.Req request = GetContentDTO.Req.builder()
                .content_id(contentId)
                .build();

        return getContentService.getContent(request);
    }

    @Operation(summary = "게시글 생성", description = "Request로 user_id, type, repo_name, repo_owner")
    @PostMapping("")
    public CreateContentDTO.Res createContent(@RequestBody CreateContentDTO.Req request) {
        return createContentService.createContent(request);
    }

    @Operation(summary = "게시글 수정", description = "Request로 content_id, title, body")
    @PatchMapping("")
    public UpdateContentDTO.Res updateContent(@RequestBody UpdateContentDTO.Req request) {
        return updateContentService.updateContent(request);
    }

    @Operation(summary = "게시글 삭제(Soft Delete)", description = "Request로 content_id")
    @DeleteMapping("")
    public DeleteContentDTO.Res deleteContent(@RequestParam Long contentId) {

        DeleteContentDTO.Req request = DeleteContentDTO.Req.builder()
                .contentId(contentId)
                .build();

        return deleteContentService.deleteContent(request);
    }
}
