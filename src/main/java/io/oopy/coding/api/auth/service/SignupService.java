package io.oopy.coding.api.auth.service;

import io.oopy.coding.common.resolver.access.AccessToken;
import io.oopy.coding.common.response.code.ErrorCode;
import io.oopy.coding.common.response.exception.GlobalErrorException;
import io.oopy.coding.common.util.jwt.entity.JwtUserInfo;
import io.oopy.coding.common.util.jwt.JwtUtil;
import io.oopy.coding.common.util.redis.forbidden.ForbiddenTokenService;
import io.oopy.coding.domain.user.dto.UserSignupReq;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.api.user.service.UserSaveService;
import io.oopy.coding.api.user.service.UserSearchService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final ForbiddenTokenService forbiddenTokenService;

    public Map<String, String> generateSignupTokens(Integer githubId) {
        JwtUserInfo jwtUserInfo = JwtUserInfo.createByGithubId(githubId);

        String accessToken = jwtUtil.generateSignupAccessToken(jwtUserInfo);
        log.info("accessToken : {}", accessToken);

        return Map.of(ACCESS_TOKEN.getValue(), accessToken);
    }

    public Map<String, String> signup(UserSignupReq dto, AccessToken accessToken) {
        Integer githubId = accessToken.githubId();

        if (userSearchService.isPresentByGithubId(githubId))
            throw new GlobalErrorException(ErrorCode.ALREADY_REGISTERED_USER);

        User user = User.of(githubId, dto.getName());
        userSaveService.save(user);
        forbiddenTokenService.register(accessToken);

        return loginService.login(user.getGithubId());
    }
}
