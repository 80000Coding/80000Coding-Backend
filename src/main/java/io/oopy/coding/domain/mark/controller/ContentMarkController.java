package io.oopy.coding.domain.mark.controller;

import io.oopy.coding.domain.mark.dto.CountMarkDTO;
import io.oopy.coding.domain.mark.service.ContentMarkService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/mark")
public class ContentMarkController {
    private final ContentMarkService contentMarkService;

    @Operation(summary = "좋아요, 북마크 개수", description = "좋아요와 북마크 개수를 한번에 return")
    @GetMapping("")
    public CountMarkDTO countMarkDTO() {
        return CountMarkDTO.builder()
                .like(3L)
                .bookmark(24L)
                .build();
    }

}
