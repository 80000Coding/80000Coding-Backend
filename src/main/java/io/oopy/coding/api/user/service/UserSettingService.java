package io.oopy.coding.api.user.service;

import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.domain.user.entity.UserSetting;
import io.oopy.coding.domain.user.repository.UserSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSettingService {

    private final UserSettingRepository userSettingRepository;

    @Transactional(readOnly = true)
    public UserSetting search(User user) {
        return userSettingRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("User Setting 데이터가 없습니다."))
        ;
    }

    @Transactional
    public void save(UserSetting userSetting) {
        userSettingRepository.save(userSetting);
    }
}
