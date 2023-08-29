package io.oopy.coding.common.redis.forbidden;

import io.oopy.coding.common.jwt.util.JwtTokenProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Component
public class ForbiddenTokenService {
    private final ForbiddenTokenRepository forbiddenTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public void register(String accessToken, Long userId) {
        final Date now = new Date();
        final Date expireDate = jwtTokenProvider.getExpiryDate(accessToken);

        final long expireTime = expireDate.getTime() - now.getTime();

        ForbiddenToken forbiddenToken = ForbiddenToken.of(accessToken, userId, expireTime);

        forbiddenTokenRepository.save(forbiddenToken);
        log.info("forbidden token registered. about Token : {}", accessToken);
    }

    public boolean isForbidden(String accessToken) {
        return forbiddenTokenRepository.existsById(accessToken);
    }
}
