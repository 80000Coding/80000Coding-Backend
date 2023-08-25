package io.oopy.coding.domain.user.api;

import io.oopy.coding.domain.user.application.UserAuthService;
import io.oopy.coding.domain.user.dto.UserAuthenticateReq;
import io.oopy.coding.global.cookie.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static io.oopy.coding.global.jwt.AuthConstants.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserAPI {
    private final UserAuthService userAuthService;
    private final CookieUtil cookieUtil;

    /**
     * OAuth2.0 인증을 통해 로그인을 진행하는 시나리오
     * 추가 정보 입력받는 단계 이전 (JWT 첫 발급 단계)
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginTest(@RequestBody UserAuthenticateReq dto) {
        Map<String, String> tokens = userAuthService.login(dto);
        cookieUtil.createCookie(REFRESH_TOKEN.getValue(), tokens.get(REFRESH_TOKEN.getValue()), 60 * 60 * 24 * 7);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(ACCESS_TOKEN.getValue(), tokens.get(ACCESS_TOKEN.getValue()))
                .build();
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue("refreshToken") String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        userAuthService.logout(request.getHeader(AUTH_HEADER.getValue()), refreshToken);
        cookieUtil.deleteCookie(request, response, REFRESH_TOKEN.getValue());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("refreshToken") String refreshToken) {
        Map<String, String> tokens = userAuthService.refresh(refreshToken);
        cookieUtil.createCookie(REFRESH_TOKEN.getValue(), tokens.get(REFRESH_TOKEN.getValue()), 60 * 60 * 24 * 7);

        return ResponseEntity.ok()
                .header(ACCESS_TOKEN.getValue(), tokens.get(ACCESS_TOKEN.getValue()))
                .build();
    }
}
