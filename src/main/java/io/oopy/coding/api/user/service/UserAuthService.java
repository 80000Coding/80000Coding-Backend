package io.oopy.coding.api.user.service;

import io.oopy.coding.common.resolver.access.AccessToken;
import io.oopy.coding.common.security.jwt.JwtProvider;
import io.oopy.coding.common.security.jwt.dto.Jwt;
import io.oopy.coding.common.security.jwt.dto.JwtAuthInfo;
import io.oopy.coding.common.security.jwt.dto.JwtSubInfo;
import io.oopy.coding.common.util.redis.forbidden.ForbiddenTokenService;
import io.oopy.coding.common.util.redis.refresh.RefreshToken;
import io.oopy.coding.common.util.redis.refresh.RefreshTokenService;
import io.oopy.coding.domain.user.dto.UserAuthReq;
import io.oopy.coding.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserAuthService {
    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;
    private final UserSearchService userSearchService;
    private final JwtProvider jwtProvider;

    public Jwt login(UserAuthReq dto) {
        User user = userSearchService.findById(dto.getId());

        return generateToken(JwtAuthInfo.from(user));
    }

    @Transactional
    public void logout(AccessToken requestAccessToken, String requestRefreshToken) {
        forbiddenTokenService.register(requestAccessToken);

        if (!StringUtils.hasText(requestRefreshToken))
            refreshTokenService.logout(requestRefreshToken);
    }

    public Jwt refresh(String requestRefreshToken) {
        RefreshToken refreshToken = refreshTokenService.refresh(requestRefreshToken);

        Long userId = refreshToken.getUserId();
        JwtSubInfo dto = JwtAuthInfo.from(userSearchService.findById(userId));
        String accessToken = jwtProvider.generateToken(dto);

        return Jwt.of(accessToken, refreshToken.getToken());
    }

    private Jwt generateToken(JwtAuthInfo jwtAuthInfo) {
        String accessToken = jwtProvider.generateToken(jwtAuthInfo);
        String refreshToken = refreshTokenService.issueRefreshToken(accessToken);
        log.debug("accessToken : {}, refreshToken : {}", accessToken, refreshToken);

        return Jwt.of(accessToken, refreshToken);
    }
}
