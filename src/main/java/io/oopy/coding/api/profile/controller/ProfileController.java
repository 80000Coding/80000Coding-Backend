package io.oopy.coding.api.profile.controller;

import io.oopy.coding.api.profile.service.ProfileService;
import io.oopy.coding.common.resolver.access.AccessToken;
import io.oopy.coding.common.resolver.access.AccessTokenInfo;
import io.oopy.coding.common.response.ErrorResponse;
import io.oopy.coding.common.response.FailureResponse;
import io.oopy.coding.common.response.SuccessResponse;
import io.oopy.coding.common.security.authentication.CustomUserDetails;
import io.oopy.coding.domain.organization.entity.Organization;
import io.oopy.coding.domain.user.dto.UserNicknameReq;
import io.oopy.coding.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
            @Parameter(name = "accessToken", description = "유저의 access token"),
            @Parameter(name = "id", description = "유저의 id")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "4xx", description = "에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> profileInfo(
                                         @AccessTokenInfo(required = false) AccessToken accessToken,
                                         @PathVariable("id") long id) {
        Map<String, ?> profileInfo = profileService.findByAccessTokenAndId(accessToken, id);
        ResponseEntity<?> responseEntity = ResponseEntity.ok().body(SuccessResponse.from(profileInfo));

        return responseEntity;
    }

    // 닉네임 변경
    @PatchMapping(value = "/nickname")
    public ResponseEntity<?> changeNickname(@AuthenticationPrincipal CustomUserDetails securityUser,
                                            @AccessTokenInfo(required = false) AccessToken accessToken,
                                            @RequestBody UserNicknameReq userNicknameReq) {
        profileService.changeNickname(securityUser.getUserId(), userNicknameReq.getNickname());
        return ResponseEntity.ok(SuccessResponse.from(null));
    }

    // 중복 로그인 확인
    @GetMapping("/duplicate")
    public ResponseEntity<?> exists(@RequestParam String nickname) {
        if(profileService.isExist(nickname)) {
            return ResponseEntity.ok(SuccessResponse.from(Map.of("nickname", "EXIST")));
        } else {
            return ResponseEntity.ok(SuccessResponse.from(Map.of("nickname", "NOT_EXIST")));
        }
    }

    // 컨트리뷰터 랭킹 뱃지 표시 변경
    @PatchMapping("/contributor-ranking-mark")
    public ResponseEntity<?> changeContributorRankingMark(@AuthenticationPrincipal CustomUserDetails securityUser) {
        profileService.changeUserContributorRankingMarkAgree(securityUser.getUserId());
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    // 이메일 수신 동의 변경
    @PatchMapping("/email-agree")
    public ResponseEntity<?> changeEmailAgree(@AuthenticationPrincipal CustomUserDetails securityUser) {
        profileService.changeUserEmailAgree(securityUser.getUserId());
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    // 푸시 메시지 수신 동의 변경
    @PatchMapping("/push-agree")
    public ResponseEntity<?> changeUserPushMessageAgree(@AuthenticationPrincipal CustomUserDetails securityUser) {
        profileService.changeUserPushMessageAgree(securityUser.getUserId());
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    // 탈퇴
    @DeleteMapping("")
    public ResponseEntity<?> delete(@AuthenticationPrincipal CustomUserDetails securityUser) {
        profileService.delete(securityUser.getUserId());
        return ResponseEntity.ok(SuccessResponse.noContent());
    }
}
