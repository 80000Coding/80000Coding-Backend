package io.oopy.coding.api.auth.service;

import io.oopy.coding.api.user.service.UserSearchService;
import io.oopy.coding.common.security.jwt.JwtProvider;
import io.oopy.coding.common.security.jwt.dto.Jwt;
import io.oopy.coding.common.security.jwt.dto.JwtAuthInfo;
import io.oopy.coding.common.security.jwt.dto.JwtSubInfo;
import io.oopy.coding.common.util.redis.refresh.RefreshTokenService;
import io.oopy.coding.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {
    private final UserSearchService userSearchService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public Jwt login(Integer githubId) {
        User user = userSearchService.findByGithubId(githubId);
        JwtSubInfo jwtUserInfo = JwtAuthInfo.from(user);
        String accessToken = jwtProvider.generateToken(jwtUserInfo);
        String refreshToken = refreshTokenService.issueRefreshToken(accessToken);
        log.info("accessToken : {}, refreshToken : {}", accessToken, refreshToken);

        return Jwt.of(accessToken, refreshToken);
    }
}