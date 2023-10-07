package io.oopy.coding.common.utils.redis.signupRefresh;

import io.oopy.coding.common.utils.jwt.entity.JwtUserInfo;
import io.oopy.coding.common.utils.jwt.exception.AuthErrorCode;
import io.oopy.coding.common.utils.jwt.exception.AuthErrorException;
import io.oopy.coding.common.utils.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
public class RefreshSignupTokenServiceImpl implements RefreshSignupTokenService {
    private final RefreshSignupTokenRepository refreshSignupTokenRepository;
    private final JwtUtil jwtUtil;
    private final Duration refreshTokenExpireTime;

    public RefreshSignupTokenServiceImpl(
            RefreshSignupTokenRepository refreshSignupTokenRepository,
            JwtUtil jwtUtil,
            @Value("${jwt.token.signup-refresh-expiration-time}") Duration refreshTokenExpireTime)
    {
        this.refreshSignupTokenRepository = refreshSignupTokenRepository;
        this.jwtUtil = jwtUtil;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    @Override
    public String issueRefreshToken(String accessToken) throws AuthErrorException {
        Integer githubId = jwtUtil.getGithubIdFromToken(accessToken);

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
        final Integer githubId = jwtUtil.getGithubIdFromToken(requestRefreshToken);
        final RefreshSignupToken refreshSignupToken = findOrThrow(githubId);

        refreshSignupTokenRepository.delete(refreshSignupToken);
        log.info("refresh token deleted. : {}", refreshSignupToken);
    }

    private String makeRefreshToken(Integer githubId) {
        JwtUserInfo jwtUserInfo = JwtUserInfo.createByGithubId(githubId);
        return jwtUtil.generateSignupRefreshToken(jwtUserInfo);
    }

    private long getExpireTime() {
        return refreshTokenExpireTime.toMillis();
    }

    private RefreshSignupToken findOrThrow(Integer githubId) {
        return refreshSignupTokenRepository.findById(githubId)
                .orElseThrow(() -> new AuthErrorException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND, "can't find refresh token"));
    }
}
