package io.oopy.coding.api.user.service;

import io.oopy.coding.common.resolver.access.AccessToken;
import io.oopy.coding.domain.user.dto.UserAuthReq;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.common.util.jwt.entity.JwtUserInfo;
import io.oopy.coding.common.util.jwt.JwtUtil;
import io.oopy.coding.common.util.redis.forbidden.ForbiddenTokenService;
import io.oopy.coding.common.util.redis.refresh.RefreshToken;
import io.oopy.coding.common.util.redis.refresh.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

import static io.oopy.coding.common.util.jwt.AuthConstants.ACCESS_TOKEN;
import static io.oopy.coding.common.util.jwt.AuthConstants.REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserAuthService {
    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;
    private final UserSearchService userSearchService;
    private final JwtUtil jwtUtil;

    public Map<String, String> login(UserAuthReq dto) {
        User user = userSearchService.findById(dto.getId());

        return generateToken(JwtUserInfo.from(user));
    }

    @Transactional
    public void logout(AccessToken requestAccessToken, String requestRefreshToken) {
        forbiddenTokenService.register(requestAccessToken);

        if (!StringUtils.hasText(requestRefreshToken))
            refreshTokenService.logout(requestRefreshToken);
    }

    public Map<String, String> refresh(String requestRefreshToken) {
        RefreshToken refreshToken = refreshTokenService.refresh(requestRefreshToken);

        Long userId = refreshToken.getUserId();
        JwtUserInfo dto = JwtUserInfo.from(userSearchService.findById(userId));
        String accessToken = jwtUtil.generateAccessToken(dto);

        return Map.of(ACCESS_TOKEN.getValue(), accessToken, REFRESH_TOKEN.getValue(), refreshToken.getToken());
    }

    private Map<String, String> generateToken(JwtUserInfo jwtUserInfo) {
        String accessToken = jwtUtil.generateAccessToken(jwtUserInfo);
        String refreshToken = refreshTokenService.issueRefreshToken(accessToken);
        log.debug("accessToken : {}, refreshToken : {}", accessToken, refreshToken);

        return Map.of(ACCESS_TOKEN.getValue(), accessToken, REFRESH_TOKEN.getValue(), refreshToken);
    }
}
