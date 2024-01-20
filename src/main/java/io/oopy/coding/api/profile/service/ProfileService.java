package io.oopy.coding.api.profile.service;

import io.oopy.coding.api.user.service.UserSaveService;
import io.oopy.coding.api.user.service.UserSearchService;
import io.oopy.coding.api.user.service.UserSettingService;
import io.oopy.coding.api.user.service.UserStatisticService;
import io.oopy.coding.common.resolver.access.AccessToken;
import io.oopy.coding.common.security.authentication.CustomUserDetails;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.domain.user.entity.UserSetting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserSearchService userSearchService;
    private final UserStatisticService userStatisticService;
    private final UserSaveService userSaveService;
    private final UserSettingService userSettingService;

    @Transactional(readOnly = true)
    public Map<String, ?> findByAccessTokenAndId(Authentication authentication, long id) {
        User user = userSearchService.findById(id);
        Boolean settingFlag;

        //github id 일치 시 설정 버튼 가능
        settingFlag = (authentication != null && ((CustomUserDetails)authentication.getPrincipal()).getUserId().equals(user.getId()))
                ? Boolean.TRUE : Boolean.FALSE;

        return Map.of(
                "settingFlag", settingFlag,
                "profileImageUrl", Optional.ofNullable(user.getProfileImageUrl()).orElse("none"),
                "name", user.getName(),
                "postCount", userStatisticService.countPostByUserId(id),
                "projectCount", userStatisticService.countProjectByUserId(id),
                "organizationCodes", user.getOrganizationCodes()
        );
    }

    /**
     * 닉네임 변경
     * @param userId
     * @param nickname
     */
    public void changeNickname(Long userId, String nickname) {
        User user = userSearchService.findById(userId);
        user.changeNickname(nickname);
        if (isExist(nickname))
            throw new IllegalArgumentException("이미 존재하는 닉네임 입니다."); // fixme exception
        userSaveService.save(user);
    }

    @Transactional
    public void changeUserContributorRankingMarkAgree(Long userId) {
        UserSetting userSetting = getByUserId(userId);
        userSetting.changeContributorRankingMarkAgree();
    }

    @Transactional
    public void changeUserEmailAgree(Long userId) {
        UserSetting userSetting = getByUserId(userId);
        userSetting.changeEmailAgree();
    }

    @Transactional
    public void changeUserPushMessageAgree(Long userId) {
        UserSetting userSetting = getByUserId(userId);
        userSetting.changePushAgree();
    }

    @Transactional
    public void delete(Long userId) {
        User user = userSearchService.findById(userId);
        user.setDeleted();
    }

    /**
     * 닉네임 중복 확인
     * @param nickname
     * @return
     */
    public boolean isExist(String nickname) {
        try {
            userSearchService.findByNickname(nickname);
            return true;
        } catch (Exception e) {
            // 존재하지 않는 유저
            // exception으로 판단하는게 옳은진 모르겠음
            return false;
        }
    }

    private UserSetting getByUserId(Long userId) {
        User user = userSearchService.findById(userId);
        return userSettingService.search(user);
    }
}
