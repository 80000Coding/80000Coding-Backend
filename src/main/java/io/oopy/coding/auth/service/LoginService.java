package io.oopy.coding.auth.service;

import io.oopy.coding.common.util.jwt.entity.JwtUserInfo;
import io.oopy.coding.common.util.jwt.JwtUtil;
import io.oopy.coding.common.util.redis.refresh.RefreshTokenService;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.user.service.UserSearchService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static io.oopy.coding.common.util.jwt.AuthConstants.ACCESS_TOKEN;
import static io.oopy.coding.common.util.jwt.AuthConstants.REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {
    private final UserSearchService userSearchService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public Map<String, String> login(Integer githubId) {
        User user = userSearchService.findByGithubId(githubId);
        JwtUserInfo jwtUserInfo = JwtUserInfo.from(user);
        String accessToken = jwtUtil.generateAccessToken(jwtUserInfo);
        String refreshToken = refreshTokenService.issueRefreshToken(accessToken);
        log.info("accessToken : {}, refreshToken : {}", accessToken, refreshToken);

        return Map.of(ACCESS_TOKEN.getValue(), accessToken, REFRESH_TOKEN.getValue(), refreshToken);
    }
}