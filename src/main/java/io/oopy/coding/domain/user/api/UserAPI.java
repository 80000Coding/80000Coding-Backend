package io.oopy.coding.domain.user.api;

import io.oopy.coding.domain.user.application.UserAuthService;
import io.oopy.coding.domain.user.application.UserSearchService;
import io.oopy.coding.domain.user.dto.TokenDto;
import io.oopy.coding.domain.user.dto.UserAuthenticateDto;
import io.oopy.coding.global.cookie.CookieUtil;
import io.oopy.coding.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserAPI {
    private final UserAuthService userAuthService;
    private final UserSearchService userSearchService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtil cookieUtil;


    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserAuthenticateDto dto) {
        TokenDto tokenDto = TokenDto.of(jwtTokenProvider.generateAccessToken(dto));
        log.info("access token: {}", tokenDto.getAccess());

        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue("accessToken") String accessToken, HttpServletRequest request, HttpServletResponse response) {
        userAuthService.logout(accessToken);
        cookieUtil.deleteCookie(request, response, "accessToken");
        return ResponseEntity.ok().build();
    }
}
