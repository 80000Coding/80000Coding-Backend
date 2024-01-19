package io.oopy.coding.api.auth.service;

import io.oopy.coding.api.user.service.UserSearchService;
import io.oopy.coding.common.security.jwt.JwtProvider;
import io.oopy.coding.common.security.jwt.dto.JwtAuthInfo;
import io.oopy.coding.common.security.jwt.dto.JwtSubInfo;
import io.oopy.coding.common.util.redis.refresh.RefreshTokenService;
import io.oopy.coding.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static io.oopy.coding.common.security.jwt.AuthConstants.ACCESS_TOKEN;
import static io.oopy.coding.common.security.jwt.AuthConstants.REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {
    private final UserSearchService userSearchService;
    private final JwtProvider jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public Map<String, String> login(Integer githubId) {
        User user = userSearchService.findByGithubId(githubId);
        JwtSubInfo jwtUserInfo = JwtAuthInfo.from(user);
        String accessToken = jwtUtil.generateToken(jwtUserInfo);
        String refreshToken = refreshTokenService.issueRefreshToken(accessToken);
        log.info("accessToken : {}, refreshToken : {}", accessToken, refreshToken);

        return Map.of(ACCESS_TOKEN.getValue(), accessToken, REFRESH_TOKEN.getValue(), refreshToken);
    }
}