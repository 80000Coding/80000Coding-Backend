package io.oopy.coding.global.redis.refresh;

import io.oopy.coding.domain.user.dao.UserRepository;
import io.oopy.coding.domain.user.domain.User;
import io.oopy.coding.domain.user.dto.UserAuthenticateDto;
import io.oopy.coding.global.jwt.JwtTokenProvider;
import io.oopy.coding.global.jwt.exception.auth.AuthErrorCode;
import io.oopy.coding.global.jwt.exception.auth.AuthErrorException;
import io.oopy.coding.global.redis.forbidden.ForbiddenToken;
import io.oopy.coding.global.redis.forbidden.ForbiddenTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final Duration refreshTokenExpireTime;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            ForbiddenTokenRepository forbiddenTokenRepository,
            @Value("${jwt.token.refresh-expiration-time}") Duration refreshTokenExpireTime)
    {
        this.refreshTokenRepository = refreshTokenRepository;
        this.forbiddenTokenRepository = forbiddenTokenRepository;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    public String refresh(String accessToken) {

    }

    /**
     * Refresh Token 발행
     *
     * @param accessToken
     * @return String : Refresh Token
     */
    public String issueRefreshToken(String accessToken) {
        final var refreshToken = RefreshToken.builder()
                .access(accessToken)
                .refresh(makeRefreshToken())
                .ttl(getExpireTime())
                .build();

        refreshTokenRepository.save(refreshToken);

        log.info("refresh token issued. about access Token : {}", accessToken);
        return refreshToken.getRefresh();
    }


    /**
     * access token 으로 refresh token을 찾아서 제거 (로그아웃)
     * @param requestAccessToken : access token
     * @param currentUserId : logout을 요청한 유저의 id
     */
    public void logout(final String requestAccessToken, Long currentUserId) {
        final RefreshToken refreshToken = findOrThrow(requestAccessToken);
        final Long expectedUserId = refreshToken.getUserId();

        if (!expectedUserId.equals(currentUserId)) {
            log.warn("mismatched user id. expected : {}, actual : {}", expectedUserId, currentUserId);
            throw new AuthErrorException(AuthErrorCode.MISMATCHED_REFRESH_TOKEN, "mismatched user id");
        }

        refreshTokenRepository.delete(refreshToken);
    }

    private String makeRefreshToken() {
        return UUID.randomUUID().toString();
    }

    private long getExpireTime() {
        return refreshTokenExpireTime.toMillis();
    }

    private RefreshToken findOrThrow(String accessToken) {
        return refreshTokenRepository.findById(accessToken).orElseThrow(
                () -> new AuthErrorException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND, "can't find refresh token"));
    }
}
