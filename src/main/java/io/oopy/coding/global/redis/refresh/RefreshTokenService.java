package io.oopy.coding.global.redis.refresh;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void save(RefreshToken refreshToken, int refreshExpireTime) {
        refreshTokenRepository.save(refreshToken, refreshExpireTime);
    }

}
