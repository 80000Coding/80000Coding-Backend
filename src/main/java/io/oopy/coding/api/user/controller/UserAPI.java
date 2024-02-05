package io.oopy.coding.api.user.controller;

import io.oopy.coding.api.user.service.UserAuthService;
import io.oopy.coding.api.user.service.UserProfileService;
import io.oopy.coding.common.resolver.access.AccessToken;
import io.oopy.coding.common.resolver.access.AccessTokenInfo;
import io.oopy.coding.common.response.SuccessResponse;
import io.oopy.coding.common.security.authentication.CustomUserDetails;
import io.oopy.coding.common.security.jwt.AuthConstants;
import io.oopy.coding.common.security.jwt.dto.Jwt;
import io.oopy.coding.common.security.jwt.exception.AuthErrorCode;
import io.oopy.coding.common.security.jwt.exception.AuthErrorException;
import io.oopy.coding.common.util.cookie.CookieUtil;
import io.oopy.coding.domain.user.dto.UserAuthReq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static io.oopy.coding.common.security.jwt.AuthConstants.ACCESS_TOKEN;
import static io.oopy.coding.common.security.jwt.AuthConstants.REFRESH_TOKEN;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserAPI {
    private final UserAuthService userAuthService;
    private final CookieUtil cookieUtil;
    private final UserProfileService userProfileService;

    /**
     * OAuth2.0 인증을 통해 로그인을 진행하는 시나리오 <br/>
     * 추가 정보 입력받는 단계 이전 (JWT 첫 발급 단계) <br/>
     * 실제로는 UserAuthReq가 아닌 userId만 받으면 됨.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginTest(@RequestBody UserAuthReq dto) {
        Jwt tokens = userAuthService.login(dto);
        ResponseCookie cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), tokens.refreshToken(), 60 * 60 * 24 * 7);

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(ACCESS_TOKEN.getValue(), tokens.accessToken())
                .build();
    }

    @GetMapping("/logout")
    @PreAuthorize("isAuthenticate()")
    public ResponseEntity<?> logoutTest(@AccessTokenInfo AccessToken accessToken,
                                        @CookieValue(value = "refreshToken", required = false) @Valid String refreshToken,
                                        HttpServletRequest request, HttpServletResponse response) {
        if (accessToken.isReissued()) {
            refreshToken = response.getHeader(HttpHeaders.SET_COOKIE).substring(AuthConstants.REFRESH_TOKEN.getValue().length() + 1);
            log.info("reissued refresh token: {}", refreshToken);
        }

        userAuthService.logout(accessToken, refreshToken);

        if (!StringUtils.hasText(refreshToken)) {
            return ResponseEntity.ok(SuccessResponse.noContent());
        }

        ResponseCookie cookie = cookieUtil.deleteCookie(request, response, REFRESH_TOKEN.getValue())
                .orElseThrow(() -> new AuthErrorException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND, "존재하지 않는 쿠키입니다."));
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(SuccessResponse.noContent());
    }

    @GetMapping("/refresh")
    @PreAuthorize("anonymous()")
    public ResponseEntity<?> refreshTest(@CookieValue("refreshToken") String refreshToken) {
        if (refreshToken == null) {
            throw new IllegalArgumentException("존재하지 않는 쿠키입니다."); // TODO : 공통 예외로 변경
        }
        Jwt tokens = userAuthService.refresh(refreshToken);
        ResponseCookie cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), tokens.refreshToken(), 60 * 60 * 24 * 7);

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(ACCESS_TOKEN.getValue(), tokens.accessToken())
                .build();
    }

    @GetMapping("/authentication")
    public ResponseEntity<?> authenticationTest(@AuthenticationPrincipal CustomUserDetails securityUser) {
        return ResponseEntity.ok(Map.of("userId", securityUser.getUserId()));
    }


    // 탈퇴
    @DeleteMapping("/{user_id}")
    @PreAuthorize("isAuthenticated() and authorManager.isSameAuthor(authentication.getPrincipal(), #userId)")
    public ResponseEntity<?> delete(@AuthenticationPrincipal CustomUserDetails securityUser, @PathVariable("user_id") Long userId) {
        userProfileService.delete(securityUser.getUserId());
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @GetMapping("/profile-image")
    @PreAuthorize("isAuthenticate()")
    public ResponseEntity<?> saveProfileImage(@AuthenticationPrincipal CustomUserDetails securityUser,
                                              @RequestParam("image") String imageUrl) {
        userProfileService.saveProfileImage(securityUser.getUserId(), imageUrl);
        return null;
    }
}
