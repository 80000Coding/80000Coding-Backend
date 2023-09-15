package io.oopy.coding.mark.controller;

import io.oopy.coding.mark.dto.CountMarkDTO;
import io.oopy.coding.mark.dto.IsPressDTO;
import io.oopy.coding.mark.service.countMarksService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/mark")
public class ContentMarkController {
    private final countMarksService countMarksService;

    @Operation(summary = "좋아요, 북마크 개수", description = "좋아요와 북마크 개수를 한번에 return")
    @GetMapping("")
    public CountMarkDTO countMarkDTO() {
        return CountMarkDTO.builder()
                .like(3L)
                .bookmark(24L)
                .build();
    }

    @Operation(summary = "유저 Press 여부", description = "0일 시 Press, 1일 시 Not press")
    @PostMapping("")
    public IsPressDTO isPressDTO() {
        return IsPressDTO.builder()
                .like(0)
                .bookmark(1)
                .build();
    }

}
