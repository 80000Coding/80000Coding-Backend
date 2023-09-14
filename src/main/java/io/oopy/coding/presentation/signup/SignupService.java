package io.oopy.coding.presentation.signup;

import io.oopy.coding.common.jwt.util.JwtTokenProvider;
import io.oopy.coding.common.redis.forbidden.ForbiddenTokenService;
import io.oopy.coding.common.redis.refresh.RefreshTokenService;
import io.oopy.coding.domain.user.dto.UserAuthReq;
import io.oopy.coding.domain.user.dto.UserSignupReq;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.user.service.UserAuthService;
import io.oopy.coding.user.service.UserSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserSearchService userSearchService;
    private final SignupSaveService signupSaveService;
    private final UserAuthService userAuthService;
    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;

    public Map<String, String> signup(UserSignupReq dto, String refreshToken, String authHeader) {
        String accessToken = jwtTokenProvider.resolveToken(authHeader);
        Integer githubId = jwtTokenProvider.getGithubIdFromToken(accessToken);

        if (userSearchService.checkByGithubId(githubId)) {
            return null;
        }

        User user = signupSaveService.save(githubId, dto);
        refreshTokenService.signupDone(refreshToken);
        forbiddenTokenService.register(accessToken, githubId);

        UserAuthReq loginDto = new UserAuthReq(user.getId(), user.getGithubId(), user.getRole());
        Map<String, String> tokens = userAuthService.login(loginDto);

        return tokens;
    }

}
