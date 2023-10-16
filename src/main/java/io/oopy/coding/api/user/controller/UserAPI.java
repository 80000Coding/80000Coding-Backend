package io.oopy.coding.api.user.controller;

import io.oopy.coding.api.user.service.UserAuthService;
import io.oopy.coding.common.resolver.access.AccessToken;
import io.oopy.coding.common.resolver.access.AccessTokenInfo;
import io.oopy.coding.common.response.SuccessResponse;
import io.oopy.coding.common.util.jwt.AuthConstants;
import io.oopy.coding.common.util.jwt.exception.AuthErrorCode;
import io.oopy.coding.common.util.jwt.exception.AuthErrorException;
import io.oopy.coding.domain.user.dto.UserAuthReq;
import io.oopy.coding.common.util.cookie.CookieUtil;
import io.oopy.coding.common.util.jwt.entity.JwtUserInfo;
import io.oopy.coding.common.security.authentication.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static io.oopy.coding.common.util.jwt.AuthConstants.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserAPI {
    private final UserAuthService userAuthService;
    private final CookieUtil cookieUtil;

    /**
     * OAuth2.0 인증을 통해 로그인을 진행하는 시나리오 <br/>
     * 추가 정보 입력받는 단계 이전 (JWT 첫 발급 단계) <br/>
     * 실제로는 UserAuthReq가 아닌 userId만 받으면 됨.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginTest(@RequestBody UserAuthReq dto) {
        Map<String, String> tokens = userAuthService.login(dto);
        log.debug("access token: {}", tokens.get(ACCESS_TOKEN.getValue()));
        log.debug("refresh token: {}", tokens.get(REFRESH_TOKEN.getValue()));
        ResponseCookie cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), tokens.get(REFRESH_TOKEN.getValue()), 60 * 60 * 24 * 7);

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(ACCESS_TOKEN.getValue(), tokens.get(ACCESS_TOKEN.getValue()))
                .build();
    }

    @GetMapping("/logout")
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
    public ResponseEntity<?> refreshTest(@CookieValue("refreshToken") String refreshToken) {
        if (refreshToken == null) {
            throw new IllegalArgumentException("존재하지 않는 쿠키입니다."); // TODO : 공통 예외로 변경
        }
        Map<String, String> tokens = userAuthService.refresh(refreshToken);
        ResponseCookie cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), tokens.get(REFRESH_TOKEN.getValue()), 60 * 60 * 24 * 7);

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(ACCESS_TOKEN.getValue(), tokens.get(ACCESS_TOKEN.getValue()))
                .build();
    }

    @GetMapping("/authentication")
    public ResponseEntity<?> authenticationTest(@AuthenticationPrincipal CustomUserDetails securityUser, Authentication authentication) {
        log.info("type: {}", authentication.getPrincipal());
        JwtUserInfo user = securityUser.toJwtUserInfo();
        log.info("user: {}", user);

        return ResponseEntity.ok(user);
    }
}
