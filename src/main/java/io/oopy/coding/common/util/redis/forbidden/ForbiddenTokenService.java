package io.oopy.coding.common.util.redis.forbidden;

import io.oopy.coding.common.resolver.access.AccessToken;
import io.oopy.coding.common.util.jwt.JwtUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Component
public class ForbiddenTokenService {
    private final ForbiddenTokenRepository forbiddenTokenRepository;

    /**
     * 토큰을 블랙 리스트에 등록합니다.
     * @param accessToken : 블랙 리스트에 등록할 액세스 토큰 객체
     */
    public void register(AccessToken accessToken) {
        final LocalDateTime now = LocalDateTime.now();
        final long timeToLive = Duration.between(now, accessToken.expiryDate()).toSeconds();

        log.info("forbidden token ttl : {}", timeToLive);

        ForbiddenToken forbiddenToken = ForbiddenToken.of(accessToken.accessToken(), accessToken.githubId(), timeToLive);
        forbiddenTokenRepository.save(forbiddenToken);
        log.info("forbidden token registered. about User : {}", forbiddenToken.getGithubId());
    }

    /**
     * 토큰이 블랙 리스트에 등록되어 있는지 확인합니다.
     * @param accessToken : 확인할 토큰
     * @return : 블랙 리스트에 등록되어 있으면 true, 아니면 false
     */
    public boolean isForbidden(String accessToken) {
        return forbiddenTokenRepository.existsById(accessToken);
    }
}