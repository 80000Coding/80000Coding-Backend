package io.oopy.coding.presentation.signup;

import io.oopy.coding.domain.entity.User;
import io.oopy.coding.domain.repository.UserRepository;
import io.oopy.coding.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public boolean signup(String name, String email, String authorizationHeader) {
        try {
            Long githubId = jwtTokenProvider.resolveToken(authorizationHeader);

            User user = User.builder()
                    .githubId(githubId.intValue())
                    .name(name)
                    .email(email)
                    .role("ROLE_USER")
                    .build();

            //userRepository.existsByGithubId(githubId);
            userRepository.save(user);
        }
        catch (Exception e) {
            return false;
            //runtimeException Enum으로 처리
        }

        return true;
    }

}
