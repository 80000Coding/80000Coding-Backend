package io.oopy.coding.presentation.signup;

import io.oopy.coding.domain.user.dto.UserSignupReq;
import io.oopy.coding.domain.user.entity.RoleType;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupSaveService {

    private final UserRepository userRepository;

    public User save(Integer githubId, UserSignupReq dto) {
        User user = User.builder()
                .githubId(githubId)
                .name(dto.getName())
                .role(RoleType.USER)
                .build();

        userRepository.save(user);

        return user;
    }

}
