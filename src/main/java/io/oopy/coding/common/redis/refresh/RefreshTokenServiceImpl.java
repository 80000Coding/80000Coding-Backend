package io.oopy.coding.common.redis.refresh;

import io.oopy.coding.common.jwt.entity.JwtUserInfo;
import io.oopy.coding.common.jwt.exception.auth.AuthErrorCode;
import io.oopy.coding.common.jwt.exception.auth.AuthErrorException;
import io.oopy.coding.common.jwt.util.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

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
    public String issueRefreshToken(String accessToken) throws AuthErrorException {
        final var user = jwtTokenProvider.getUserInfoFromToken(accessToken);

        final var refreshToken = RefreshToken.builder()
                .userId(user.id())
                .token(makeRefreshToken(user))
                .ttl(getExpireTime())
                .build();

        refreshTokenRepository.save(refreshToken);
        log.debug("refresh token issued. : {}", refreshToken);
        return refreshToken.getToken();
    }

    @Override
    public RefreshToken refresh(String requestRefreshToken) throws AuthErrorException {
        final Long userId = jwtTokenProvider.getUserIdFromToken(requestRefreshToken);
        final RefreshToken refreshToken = findOrThrow(userId);

        validateToken(requestRefreshToken, refreshToken);

        final var user = jwtTokenProvider.getUserInfoFromToken(requestRefreshToken);
        refreshToken.rotation(makeRefreshToken(user));
        refreshTokenRepository.save(refreshToken);

        log.debug("refresh token reissued. : {}", refreshToken);
        return refreshToken;
    }

    @Override
    public void logout(String requestRefreshToken) {
        final Long userId = jwtTokenProvider.getUserIdFromToken(requestRefreshToken);
        final RefreshToken refreshToken = findOrThrow(userId);

        refreshTokenRepository.delete(refreshToken);
        log.info("refresh token deleted. : {}", refreshToken);
    }

    private String makeRefreshToken(JwtUserInfo user) {
        return jwtTokenProvider.generateRefreshToken(user);
    }

    private long getExpireTime() {
        return refreshTokenExpireTime.toMillis();
    }

    private RefreshToken findOrThrow(Long userId) {
        return refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new AuthErrorException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND, "can't find refresh token"));
    }

    private void validateToken(String requestRefreshToken, RefreshToken refreshToken) throws AuthErrorException {
        final String expectedRequestRefreshToken = refreshToken.getToken();

        if (isTakenAway(requestRefreshToken, expectedRequestRefreshToken)) {
            refreshTokenRepository.delete(refreshToken);

            final String errorMessage = String.format("mismatched refresh token. expected : %s, actual : %s", requestRefreshToken, expectedRequestRefreshToken);
            log.warn(errorMessage);
            log.info("refresh token deleted. : {}", refreshToken);
            throw new AuthErrorException(AuthErrorCode.MISMATCHED_REFRESH_TOKEN, errorMessage);
        }
    }

    private boolean isTakenAway(String requestRefreshToken, String expectedRequestRefreshToken) {
        return !requestRefreshToken.equals(expectedRequestRefreshToken);
    }
}
