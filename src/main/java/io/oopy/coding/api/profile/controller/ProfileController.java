package io.oopy.coding.api.profile.controller;

import io.oopy.coding.api.profile.service.ProfileService;
import io.oopy.coding.common.resolver.access.AccessToken;
import io.oopy.coding.common.resolver.access.AccessTokenInfo;
import io.oopy.coding.common.response.ErrorResponse;
import io.oopy.coding.common.response.FailureResponse;
import io.oopy.coding.common.response.SuccessResponse;
import io.oopy.coding.common.security.authentication.CustomUserDetails;
import io.oopy.coding.domain.organization.entity.Organization;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.oopy.coding.common.util.jwt.AuthConstants.ACCESS_TOKEN;

@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final ProfileService profileService;

    @Operation(summary = "유저 프로필 정보 조회", description = "해당 유저와 관련된 정보를 불러온다.")
    @Parameters({
            @Parameter(name = "id", description = "유저의 id"),
            @Parameter(name = "accessToken", description = "유저의 access token")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "4xx", description = "에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> profileInfo(
//                                        @RequestParam(required = true) @AccessTokenInfo AccessToken accessToken,
//                                         @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         @PathVariable("id") long id) {
        User user = profileService.findById(id);
        boolean settingFlag = true;
        //(user.getGithubId() == accessToken.githubId());

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();

        System.out.println(settingFlag);
        System.out.println(user.getProfileImageUrl());
        System.out.println(user.getName());
        System.out.println(user.getPostCount());
        System.out.println(user.getProjectCount());
        System.out.println(user.getOrganizationCodes());
        // TODO : map을 service에서 return response, service에서 분기처리
        // dev -> pull, feat -> git merge
        responseBuilder.body(SuccessResponse.from(Map.of(
                "setting_flag", settingFlag,
                "profile_image_url", "none",
                "name", user.getName(),
                "post_count", user.getPostCount(),
                "project_count", user.getProjectCount(),
                "organization_codes", user.getOrganizationCodes()
                )
        ));

        ResponseEntity<?> responseEntity = responseBuilder.build();

        return responseBuilder.build();
    }
}
