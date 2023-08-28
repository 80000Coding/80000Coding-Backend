package io.oopy.coding.user.service;

import io.oopy.coding.domain.user.dto.UserAuthReq;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.global.jwt.entity.JwtUserInfo;
import io.oopy.coding.global.jwt.util.JwtTokenProvider;
import io.oopy.coding.global.redis.forbidden.ForbiddenTokenService;
import io.oopy.coding.global.redis.refresh.RefreshToken;
import io.oopy.coding.global.redis.refresh.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static io.oopy.coding.global.jwt.AuthConstants.ACCESS_TOKEN;
import static io.oopy.coding.global.jwt.AuthConstants.REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserAuthService {
    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;
    private final UserSearchService userSearchService;

    private final JwtTokenProvider jwtTokenProvider;

    public Map<String, String> login(UserAuthReq dto) {
        User user = userSearchService.findById(dto.getId());
        JwtUserInfo jwtUserInfo = JwtUserInfo.from(user);

        String accessToken = jwtTokenProvider.generateAccessToken(jwtUserInfo);
        String refreshToken = refreshTokenService.issueRefreshToken(accessToken);
        log.debug("accessToken : {}, refreshToken : {}", accessToken, refreshToken);

        return Map.of(ACCESS_TOKEN.getValue(), accessToken, REFRESH_TOKEN.getValue(), refreshToken);
    }

    public void logout(String authHeader, String refreshToken) {
        String accessToken = jwtTokenProvider.resolveToken(authHeader);
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);

        refreshTokenService.logout(userId, refreshToken);
        forbiddenTokenService.register(accessToken, userId);
    }

    public Map<String, String> refresh(String requestRefreshToken) {
        RefreshToken refreshToken = refreshTokenService.refresh(requestRefreshToken);

        Long userId = refreshToken.getUserId();
        JwtUserInfo dto = JwtUserInfo.from(userSearchService.findById(userId));
        String accessToken = jwtTokenProvider.generateAccessToken(dto);

        return Map.of(ACCESS_TOKEN.getValue(), accessToken, REFRESH_TOKEN.getValue(), refreshToken.getToken());
    }
}
