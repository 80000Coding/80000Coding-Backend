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

    /**
     * 토큰을 블랙 리스트에 등록합니다.
     * @param accessToken : 블랙 리스트에 등록할 토큰
     * @param githubId : 블랙 리스트에 등록할 github ID
     */
    public void register(String accessToken, Integer githubId) {
        final Date now = new Date();
        final Date expireDate = jwtTokenProvider.getExpiryDate(accessToken);

        final long expireTime = expireDate.getTime() - now.getTime();

        ForbiddenToken forbiddenToken = ForbiddenToken.of(accessToken, githubId, expireTime);

        forbiddenTokenRepository.save(forbiddenToken);
        log.info("forbidden token registered. about Token : {}", accessToken);
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
