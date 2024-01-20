package io.oopy.coding.api.auth.controller;

import io.oopy.coding.api.auth.service.SignupService;
import io.oopy.coding.common.response.ErrorResponse;
import io.oopy.coding.common.response.FailureResponse;
import io.oopy.coding.common.response.SuccessResponse;
import io.oopy.coding.common.util.cookie.CookieUtil;
import io.oopy.coding.domain.user.dto.UserSignupReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static io.oopy.coding.common.security.jwt.AuthConstants.REFRESH_TOKEN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/oauth")
public class SignupController {
    private final SignupService signupService;
    private final CookieUtil cookieUtil;

    @Operation(summary = "새로운 회원에 대한 가입진행", description = "회원가입 후 제대로 추가되었다면 OK, 아니라면 BAD_REQUEST를 반환한다.")
    @Parameters({
            @Parameter(name = "user_id", description = "회원가입을 진행하려는 사용자 github_id", in = ParameterIn.PATH),
            @Parameter(name = "Authorization", description = "OAuth2로 로그인 후 발급해준 토큰", in = ParameterIn.HEADER)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "로그아웃 실패", content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "4xx", description = "에러", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/users/{user_id}")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> signup(
            @PathVariable("user_id") Integer githubId,
            @RequestHeader("Authorization") String oauthToken,
            @RequestBody @Valid UserSignupReq dto
    ) {
        Map<String, String> tokens = signupService.signup(dto, githubId, oauthToken);
        ResponseCookie cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), tokens.get(REFRESH_TOKEN.getValue()), 60 * 60 * 24 * 7);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(SuccessResponse.noContent());
    }
}
