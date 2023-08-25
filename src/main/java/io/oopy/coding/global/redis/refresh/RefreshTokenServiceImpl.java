package io.oopy.coding.global.redis.refresh;

import io.oopy.coding.global.jwt.JwtTokenProvider;
import io.oopy.coding.global.jwt.exception.auth.AuthErrorCode;
import io.oopy.coding.global.jwt.exception.auth.AuthErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Component
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final Duration refreshTokenExpireTime;

    public RefreshTokenServiceImpl(
            RefreshTokenRepository refreshTokenRepository,
            JwtTokenProvider jwtTokenProvider,
            @Value("${jwt.token.refresh-expiration-time}") Duration refreshTokenExpireTime)
    {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    @Override
    public String issueRefreshToken(Long userId) {
        final var refreshToken = RefreshToken.builder()
                .token(makeRefreshToken())
                .userId(userId)
                .ttl(getExpireTime())
                .build();

        refreshTokenRepository.save(refreshToken);
        log.debug("refresh token issued. : {}", refreshToken);
        return refreshToken.getToken();
    }

    @Override
    public RefreshToken refresh(String requestRefreshToken) {
        final var refreshToken = findOrThrow(requestRefreshToken);
        refreshToken.rotation(makeRefreshToken());

        log.debug("refresh token issued. : {}", refreshToken);
        return refreshToken;
    }

    @Override
    public void logout(Long currentUserId, String requestRefreshToken) {
        final var refreshToken = findOrThrow(requestRefreshToken);
        final Long expectedUserId = refreshToken.getUserId();
        log.debug("expected user id : {}, current user id : {}", expectedUserId, currentUserId);

        if (!expectedUserId.equals(currentUserId)) {
            final String errorMessage = String.format("mismatched user id. expected : %s, actual : %s", expectedUserId, currentUserId);
            log.warn(errorMessage);
            throw new AuthErrorException(AuthErrorCode.MISMATCHED_REFRESH_TOKEN, errorMessage);
        }

        refreshTokenRepository.delete(refreshToken);
        log.info("refresh token deleted. : {}", refreshToken);
    }

    private String makeRefreshToken() {
        return UUID.randomUUID().toString();
    }

    private long getExpireTime() {
        return refreshTokenExpireTime.toMillis();
    }

    private RefreshToken findOrThrow(String refreshToken) {
        return refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new AuthErrorException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND, "can't find refresh token"));
    }
}
