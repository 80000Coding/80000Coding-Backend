package io.oopy.coding.api.user.service;

import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.domain.user.entity.UserSetting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserProfileService {

    private final UserSearchService userSearchService;
    private final UserSaveService userSaveService;
    private final UserSettingService userSettingService;

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
