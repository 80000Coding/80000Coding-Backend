package io.oopy.coding.domain.user.application;

import io.oopy.coding.domain.user.dao.UserRepository;
import io.oopy.coding.global.cookie.CookieUtil;
import io.oopy.coding.global.jwt.JwtTokenProvider;
import io.oopy.coding.global.redis.forbidden.ForbiddenTokenService;
import io.oopy.coding.global.redis.refresh.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static io.oopy.coding.global.jwt.AuthConstants.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAuthService {
    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userAuthRepository;

    public void logout(String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        refreshTokenService.logout(token, userId);
        forbiddenTokenService.register(token, userId);
    }
}
