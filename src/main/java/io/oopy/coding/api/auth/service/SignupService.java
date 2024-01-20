package io.oopy.coding.api.auth.service;

import io.oopy.coding.api.user.service.UserSaveService;
import io.oopy.coding.api.user.service.UserSearchService;
import io.oopy.coding.api.user.service.UserSettingService;
import io.oopy.coding.common.response.code.ErrorCode;
import io.oopy.coding.common.response.exception.GlobalErrorException;
import io.oopy.coding.common.security.jwt.JwtProvider;
import io.oopy.coding.common.security.jwt.dto.JwtOauthInfo;
import io.oopy.coding.common.security.jwt.dto.JwtSubInfo;
import io.oopy.coding.common.security.jwt.qualifier.OauthRegisterTokenQualifier;
import io.oopy.coding.common.util.redis.forbidden.ForbiddenTokenService;
import io.oopy.coding.domain.user.dto.UserSignupReq;
import io.oopy.coding.domain.user.entity.RoleType;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.domain.user.entity.UserSetting;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static io.oopy.coding.common.security.jwt.AuthConstants.ACCESS_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional

public class SignupService {
    private final LoginService loginService;
    private final UserSearchService userSearchService;
    private final UserSaveService userSaveService;
    @OauthRegisterTokenQualifier
    private final JwtProvider jwtProvider;
    private final ForbiddenTokenService forbiddenTokenService;
    private final UserSettingService userSettingService;

    public Map<String, String> generateSignupTokens(Integer githubId) {
        JwtSubInfo dto = JwtOauthInfo.of(githubId, RoleType.USER);

        String accessToken = jwtProvider.generateToken(dto);
        log.info("oauth signup accessToken : {}", accessToken);

        return Map.of(ACCESS_TOKEN.getValue(), accessToken);
    }

    public Map<String, String> signup(UserSignupReq dto, Integer githubId, String oauthToken) {
        if (userSearchService.isPresentByGithubId(githubId))
            throw new GlobalErrorException(ErrorCode.ALREADY_REGISTERED_USER);

        if (jwtProvider.getSubInfoFromToken(oauthToken).githubId().equals(githubId))
            throw new GlobalErrorException(ErrorCode.MISSING_GITHUB_ID_REQUEST);

        User user = User.of(githubId, dto.getName());
        userSaveService.save(user);
        UserSetting userSetting = UserSetting.from(user);
        userSettingService.save(userSetting);

        forbiddenTokenService.register(Long.valueOf(githubId), oauthToken, jwtProvider.getExpiryDate(oauthToken));
        return loginService.login(user.getGithubId());
    }
}
