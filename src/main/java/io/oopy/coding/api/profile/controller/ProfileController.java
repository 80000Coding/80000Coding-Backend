package io.oopy.coding.api.profile.controller;

import io.oopy.coding.api.profile.service.ProfileService;
import io.oopy.coding.common.response.ErrorResponse;
import io.oopy.coding.common.response.FailureResponse;
import io.oopy.coding.common.response.SuccessResponse;
import io.oopy.coding.common.security.authentication.CustomUserDetails;
import io.oopy.coding.domain.user.dto.UserNicknameReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final ProfileService profileService;

    @Operation(summary = "유저 프로필 정보 조회", description = "해당 유저와 관련된 정보를 불러온다.")
    @Parameters({
            @Parameter(name = "accessToken", description = "유저의 access token (전송하지 않으면 비로그인 유저)", required = false),
            @Parameter(name = "id", description = "프로필 조회할 유저의 id")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "4xx", description = "에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> profileInfo(@PathVariable("id") Long id, Authentication authentication) {
        Map<String, ?> profileInfo = profileService.findByAccessTokenAndId(authentication, id);
        ResponseEntity<?> responseEntity = ResponseEntity.ok().body(SuccessResponse.from(profileInfo));

        return responseEntity;
    }

    @Operation(summary = "닉네임 변경", description = "로그인한 유저의 닉네임을 변경한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "4xx", description = "에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping(value = "/nickname")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changeNickname(@AuthenticationPrincipal CustomUserDetails securityUser,
//                                            @AccessTokenInfo(required = false) @Parameter(hidden = true) AccessToken accessToken,
                                            @RequestBody UserNicknameReq userNicknameReq) {
        profileService.changeNickname(securityUser.getUserId(), userNicknameReq.getNickname());
        return ResponseEntity.ok(SuccessResponse.from(null));
    }

    @Operation(summary = "중복로그인 확인", description = "로그인한 유저의 닉네임을 변경한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 변경 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "닉네임 변경 실패", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "4xx", description = "에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/duplicate")
    @PreAuthorize("isAuthenticated() and hasRole(ROLE_USER)")
    public ResponseEntity<?> exists(@RequestParam String nickname) {
        if(profileService.isExist(nickname)) {
            return ResponseEntity.ok(SuccessResponse.from(Map.of("nickname", "EXIST")));
        } else {
            return ResponseEntity.ok(SuccessResponse.from(Map.of("nickname", "NOT_EXIST")));
        }
    }

    @Operation(summary = "컨트리뷰터 랭킹 뱃지 표시 변경", description = "로그인한 유저의 컨트리뷰터 랭킹 뱃지 표시 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 변경 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "닉네임 변경 실패", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "4xx", description = "에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/contributor-ranking-mark")
    @PreAuthorize("isAuthenticated() and hasRole(ROLE_USER)")
    public ResponseEntity<?> changeContributorRankingMark(@AuthenticationPrincipal CustomUserDetails securityUser) {
        profileService.changeUserContributorRankingMarkAgree(securityUser.getUserId());
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "이메일 수신 동의 변경", description = "로그인한 유저의 이메일 수신 동의 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 변경 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "닉네임 변경 실패", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "4xx", description = "에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/email-agree")
    @PreAuthorize("isAuthenticated() and hasRole(ROLE_USER)")
    public ResponseEntity<?> changeEmailAgree(@AuthenticationPrincipal CustomUserDetails securityUser) {
        profileService.changeUserEmailAgree(securityUser.getUserId());
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "푸시 메시지 수신 동의 변경", description = "로그인한 유저의 푸시 메시지 수신 동의 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 변경 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "닉네임 변경 실패", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "4xx", description = "에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/push-agree")
    @PreAuthorize("isAuthenticated() and hasRole(ROLE_USER)")
    public ResponseEntity<?> changeUserPushMessageAgree(@AuthenticationPrincipal CustomUserDetails securityUser) {
        profileService.changeUserPushMessageAgree(securityUser.getUserId());
        return ResponseEntity.ok(SuccessResponse.noContent());
    }
}
