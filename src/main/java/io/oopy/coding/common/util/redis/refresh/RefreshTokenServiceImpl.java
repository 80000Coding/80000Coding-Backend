package io.oopy.coding.common.util.redis.refresh;

import io.oopy.coding.common.security.jwt.JwtProvider;
import io.oopy.coding.common.security.jwt.dto.JwtSubInfo;
import io.oopy.coding.common.security.jwt.exception.AuthErrorCode;
import io.oopy.coding.common.security.jwt.exception.AuthErrorException;
import io.oopy.coding.common.security.jwt.qualifier.RefreshTokenQualifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider refreshTokenProvider;
    private final Duration refreshTokenExpireTime;

    public RefreshTokenServiceImpl(
            RefreshTokenRepository refreshTokenRepository,
            @RefreshTokenQualifier JwtProvider refreshTokenProvider,
            @Value("${jwt.token.refresh-expiration-time}") Duration refreshTokenExpireTime)
    {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenProvider = refreshTokenProvider;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    @Override
    public String issueRefreshToken(JwtSubInfo jwtUserInfo) throws AuthErrorException {
        final var refreshToken = RefreshToken.builder()
                .userId(jwtUserInfo.id())
                .token(makeRefreshToken(jwtUserInfo))
                .ttl(getExpireTime())
                .build();

        refreshTokenRepository.save(refreshToken);
        log.debug("refresh token issued. : {}", refreshToken);
        return refreshToken.getToken();
    }

    @Override
    public RefreshToken refresh(String requestRefreshToken) throws AuthErrorException {
        final JwtSubInfo user = refreshTokenProvider.getSubInfoFromToken(requestRefreshToken);
        final RefreshToken refreshToken = findOrThrow(user.id());

        validateToken(requestRefreshToken, refreshToken);

        refreshToken.rotation(makeRefreshToken(user));
        refreshTokenRepository.save(refreshToken);

        log.debug("refresh token reissued. : {}", refreshToken);
        return refreshToken;
    }

    @Override
    public void logout(String requestRefreshToken) {
        final Long userId = refreshTokenProvider.getSubInfoFromToken(requestRefreshToken).id();
        final RefreshToken refreshToken = findOrThrow(userId);

        refreshTokenRepository.delete(refreshToken);
        log.info("refresh token deleted. : {}", refreshToken);
    }

    private String makeRefreshToken(JwtSubInfo user) {
        return refreshTokenProvider.generateToken(user);
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
