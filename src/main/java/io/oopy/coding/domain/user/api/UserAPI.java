package io.oopy.coding.domain.user.api;

import io.oopy.coding.domain.user.application.UserAuthService;
import io.oopy.coding.domain.user.application.UserSearchService;
import io.oopy.coding.domain.user.dto.TokenDto;
import io.oopy.coding.domain.user.dto.UserAuthenticateDto;
import io.oopy.coding.global.jwt.JwtTokenProviderImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserAPI {
    private final UserAuthService userAuthService;
    private final UserSearchService userSearchService;
    private final JwtTokenProviderImpl jwtTokenProviderImpl;
    private static RedisTemplate<String, String> redisTemplate;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserAuthenticateDto dto) {
        TokenDto tokenDto = TokenDto.of(
                jwtTokenProviderImpl.generateAccessToken(dto),
                jwtTokenProviderImpl.generateRefreshToken(dto)
        );
        log.info("access token: {}", tokenDto.getAccess());
        log.info("refresh token: {}", tokenDto.getRefresh());

        return ResponseEntity.ok(tokenDto);
    }

    @GetMapping("/test")
    @Secured("ROLE_USER")
    public ResponseEntity<?> test(@RequestHeader("Authorization") String header) {
        log.info("header : {}", header);
        String refreshToken = redisTemplate.opsForValue().get("1");
        log.info("refresh token : {}", refreshToken);

        return ResponseEntity.ok("성공");
    }
}
