package io.oopy.coding.common.util.redis.forbidden;

import io.oopy.coding.common.resolver.access.AccessToken;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Service
public class ForbiddenTokenService {
    private final ForbiddenTokenRepository forbiddenTokenRepository;

    /**
     * 토큰을 블랙 리스트에 등록합니다.
     * @param accessToken : 블랙 리스트에 등록할 액세스 토큰 객체
     */
    public void register(AccessToken accessToken) {
        register(accessToken.id(), accessToken.accessToken(), accessToken.expiryDate());
    }

    /**
     * 사용자 임의로 블랙 리스트에 등록할 토큰 정보를 기입합니다.
     */
    public void register(Long id, String accessToken, LocalDateTime expiryDate) {
        final LocalDateTime now = LocalDateTime.now();
        final long ttl = Duration.between(now, expiryDate).toSeconds();
        log.info("forbidden token ttl : {}", ttl);

        ForbiddenToken forbiddenToken = ForbiddenToken.of(accessToken, id, ttl);
        forbiddenTokenRepository.save(forbiddenToken);
        log.info("forbidden token registered. about User : {}", forbiddenToken.getId());
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
