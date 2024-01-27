package io.oopy.coding.api.auth.service;

import io.oopy.coding.api.user.service.UserSaveService;
import io.oopy.coding.api.user.service.UserSearchService;
import io.oopy.coding.api.user.service.UserSettingService;
import io.oopy.coding.common.response.code.ErrorCode;
import io.oopy.coding.common.response.exception.GlobalErrorException;
import io.oopy.coding.common.security.jwt.JwtProvider;
import io.oopy.coding.common.security.jwt.dto.Jwt;
import io.oopy.coding.common.security.jwt.dto.JwtAuthInfo;
import io.oopy.coding.common.security.jwt.dto.JwtOauthInfo;
import io.oopy.coding.common.security.jwt.dto.JwtSubInfo;
import io.oopy.coding.common.security.jwt.qualifier.OauthRegisterTokenQualifier;
import io.oopy.coding.common.util.redis.forbidden.ForbiddenTokenService;
import io.oopy.coding.common.util.redis.refresh.RefreshTokenService;
import io.oopy.coding.domain.user.dto.UserSignRes;
import io.oopy.coding.domain.user.dto.UserSignupReq;
import io.oopy.coding.domain.user.entity.RoleType;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.domain.user.entity.UserSetting;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional

public class SignupService {
    private final LoginService loginService;
    private final UserSearchService userSearchService;
    private final UserSaveService userSaveService;
    private final JwtProvider accessTokenProvider;
    private final RefreshTokenService refreshTokenService;
    @OauthRegisterTokenQualifier
    private final JwtProvider oauthJwtProvider;
    private final ForbiddenTokenService forbiddenTokenService;
    private final UserSettingService userSettingService;

    public UserSignRes generateSignupTokens(Integer githubId) {
        JwtSubInfo dto = JwtOauthInfo.of(githubId, RoleType.USER);

        String accessToken = oauthJwtProvider.generateToken(dto);
        log.info("oauth signup accessToken : {}", accessToken);

        return UserSignRes.ofSignUp(githubId, Jwt.of(accessToken, null));
    }

    public UserSignRes signup(UserSignupReq dto, Integer githubId, String oauthToken) {
        if (userSearchService.isPresentByGithubId(githubId))
            throw new GlobalErrorException(ErrorCode.ALREADY_REGISTERED_USER);

        if (oauthJwtProvider.getSubInfoFromToken(oauthToken).githubId().equals(githubId))
            throw new GlobalErrorException(ErrorCode.MISSING_GITHUB_ID_REQUEST);

        User user = User.of(githubId, dto.getName());
        userSaveService.save(user);
        UserSetting userSetting = UserSetting.from(user);
        userSettingService.save(userSetting);

        forbiddenTokenService.register(Long.valueOf(githubId), oauthToken, oauthJwtProvider.getExpiryDate(oauthToken));

        JwtSubInfo jwtUserInfo = JwtAuthInfo.from(user);
        String accessToken = accessTokenProvider.generateToken(jwtUserInfo);
        String refreshToken = refreshTokenService.issueRefreshToken(accessToken);
        return loginService.login(user.getGithubId());
    }
}
