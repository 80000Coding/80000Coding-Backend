package io.oopy.coding.auth.service;

import io.oopy.coding.common.util.jwt.entity.JwtUserInfo;
import io.oopy.coding.common.util.jwt.JwtUtil;
import io.oopy.coding.common.util.redis.forbidden.ForbiddenTokenService;
import io.oopy.coding.common.util.redis.signupRefresh.RefreshSignupTokenService;
import io.oopy.coding.domain.user.dto.UserSignupReq;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.user.service.UserSaveService;
import io.oopy.coding.user.service.UserSearchService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static io.oopy.coding.common.util.jwt.AuthConstants.ACCESS_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional

public class SignupService {
    private final LoginService loginService;
    private final UserSearchService userSearchService;
    private final UserSaveService userSaveService;
    private final JwtUtil jwtUtil;
    private final RefreshSignupTokenService refreshSignupTokenService;
    private final ForbiddenTokenService forbiddenTokenService;

    public Map<String, String> generateSignupTokens(Integer githubId) {
        JwtUserInfo jwtUserInfo = JwtUserInfo.createByGithubId(githubId);

        String accessToken = jwtUtil.generateSignupAccessToken(jwtUserInfo);
        //String refreshToken = refreshSignupTokenService.issueRefreshToken(accessToken);
        log.info("accessToken : {}", accessToken);

        return Map.of(ACCESS_TOKEN.getValue(), accessToken);
    }

    public Map<String, String> signup(UserSignupReq dto, String authHeader) {
        String accessToken = jwtUtil.resolveToken(authHeader);
        Integer githubId = jwtUtil.getGithubIdFromToken(accessToken);

        Map<String, String> tokens = new HashMap<>();

        if (userSearchService.isPresentByGithubId(githubId)) {
            return tokens;
        }

        User user = User.of(githubId, dto.getName());
        userSaveService.save(user);
        forbiddenTokenService.register(accessToken, githubId);

        tokens = loginService.login(user.getGithubId());

        return tokens;
    }
}
