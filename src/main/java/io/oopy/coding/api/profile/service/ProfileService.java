package io.oopy.coding.api.profile.service;

import io.oopy.coding.api.user.service.UserSearchService;
import io.oopy.coding.common.resolver.access.AccessToken;
import io.oopy.coding.common.util.jwt.JwtUtil;
import io.oopy.coding.domain.organization.entity.Organization;
import io.oopy.coding.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserSearchService userSearchService;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public User findById(long id) {
        // TODO : user가 없어서 error 가 throw 될 시 어떻게 처리할지
        return userSearchService.findById(id);
    }
}
