package io.oopy.coding.presentation.login;

import io.oopy.coding.domain.user.dto.UserAuthReq;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.user.service.UserAuthService;
import io.oopy.coding.user.service.UserSearchService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OauthService {
    private final UserSearchService userSearchService;
    private final UserAuthService userAuthService;

    public Map<String, String> login(Integer githubId) {
        User user = userSearchService.findByGithubId(githubId);
        UserAuthReq loginDto = new UserAuthReq(user.getId(), user.getGithubId(), user.getRole());

        Map<String, String> tokens = userAuthService.login(loginDto);

        return tokens;
    }

    public Map<String, String> signup(Integer githubId) {
        Map<String, String> tokens = userAuthService.signup(githubId);

        return tokens;
    }
}