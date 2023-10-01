package io.oopy.coding.common.redis.signupRefresh;

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
public class RefreshSignupTokenServiceImpl implements RefreshSignupTokenService {
    private final RefreshSignupTokenRepository refreshSignupTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final Duration refreshTokenExpireTime;

    public RefreshSignupTokenServiceImpl(
            RefreshSignupTokenRepository refreshSignupTokenRepository,
            JwtTokenProvider jwtTokenProvider,
            @Value("${jwt.token.signup-refresh-expiration-time}") Duration refreshTokenExpireTime)
    {
        this.refreshSignupTokenRepository = refreshSignupTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    @Override
    public String issueRefreshToken(String accessToken) throws AuthErrorException {
        Integer githubId = jwtTokenProvider.getGithubIdFromToken(accessToken);

        final var refreshToken = RefreshSignupToken.builder()
                .githubId(githubId)
                .token(makeRefreshToken(githubId))
                .ttl(getExpireTime())
                .build();

        refreshSignupTokenRepository.save(refreshToken);
        log.debug("refresh Signup token issued. : {}", refreshToken);
        return refreshToken.getToken();
    }
    @Override
    public void signupDone(String requestRefreshToken) {
        final Integer githubId = jwtTokenProvider.getGithubIdFromToken(requestRefreshToken);
        final RefreshSignupToken refreshSignupToken = findOrThrow(githubId);

        refreshSignupTokenRepository.delete(refreshSignupToken);
        log.info("refresh token deleted. : {}", refreshSignupToken);
    }

    private String makeRefreshToken(Integer githubId) {
        JwtUserInfo jwtUserInfo = JwtUserInfo.createByGithubId(githubId);
        return jwtTokenProvider.generateSignupRefreshToken(jwtUserInfo);
    }

    private long getExpireTime() {
        return refreshTokenExpireTime.toMillis();
    }

    private RefreshSignupToken findOrThrow(Integer githubId) {
        return refreshSignupTokenRepository.findById(githubId)
                .orElseThrow(() -> new AuthErrorException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND, "can't find refresh token"));
    }
}
