package io.oopy.coding.api.profile.service;

import io.oopy.coding.api.user.service.UserSearchService;
import io.oopy.coding.common.resolver.access.AccessToken;
import io.oopy.coding.common.util.jwt.JwtUtil;
import io.oopy.coding.common.util.jwt.entity.JwtUserInfo;
import io.oopy.coding.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {
    private final UserSearchService userSearchService;
    private final JwtUtil jwtUtil;

    public User findById(long id) {
        // TODO : user가 없어서 error 가 throw 될 시 어떻게 처리할지
        return userSearchService.findById(id);
    }

    // TODO : user id 를 여기서 이렇게 찾는게 맞을지
    public long getUserId(AccessToken accessToken) {
        JwtUserInfo userInfo = jwtUtil.getUserInfoFromToken(accessToken.accessToken());

        return userInfo.id();
    }
}
