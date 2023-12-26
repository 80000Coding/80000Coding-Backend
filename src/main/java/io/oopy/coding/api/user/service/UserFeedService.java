package io.oopy.coding.api.user.service;

import io.oopy.coding.domain.user.repository.UserQueryRepository;
import io.oopy.coding.domain.user.dto.UserDTO;
import io.oopy.coding.domain.user.dto.UserFeedSearchReq;
import io.oopy.coding.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFeedService {

    private final UserQueryRepository userQueryRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> searchByNickname(UserFeedSearchReq.Nickname search, Pageable pageable) {
        Page<User> users = userQueryRepository.findByNickname(search.getNickname(), pageable);
        return getUserDTO(users);
    }

    // private //
    public Page<UserDTO> getUserDTO(Page<User> users) {
        return users.map(v-> {
            UserDTO userDTO = UserDTO.toDTO(v);
            return userDTO;
        });
    }
}
