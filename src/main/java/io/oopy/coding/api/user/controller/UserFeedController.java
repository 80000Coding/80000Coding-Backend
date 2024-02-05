package io.oopy.coding.api.user.controller;

import io.oopy.coding.api.user.service.UserFeedService;
import io.oopy.coding.common.response.SuccessResponse;
import io.oopy.coding.domain.user.dto.UserDTO;
import io.oopy.coding.domain.user.dto.UserFeedSearchReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "유저 피드", description = "유저 피드 API")
@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class UserFeedController {

    private final UserFeedService userFeedService;

    @Operation(summary = "유저 닉네임으로 검색", description = "유저 닉네임으로 검색")
    @GetMapping("/nickname")
    @PreAuthorize("permitAll()")
    public ResponseEntity<SuccessResponse<List<UserDTO> > > userFeedListByNickname(UserFeedSearchReq.Nickname userFeedSearchReq, @PageableDefault Pageable pageable) {
        Page<UserDTO> userDtos = userFeedService.searchByNickname(userFeedSearchReq, pageable);
        return ResponseEntity.ok(SuccessResponse.of(userDtos.getContent(), userDtos.getTotalPages(), userDtos.getTotalElements()));
    }
}