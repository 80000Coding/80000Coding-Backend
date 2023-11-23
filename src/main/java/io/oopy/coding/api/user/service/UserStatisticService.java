package io.oopy.coding.api.user.service;

import io.oopy.coding.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Formula;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserStatisticService {
    private final UserRepository userRepository;

    public long countPostByUserId(long id) {
        //@Formula("(SELECT COUNT(*) FROM CONTENT C WHERE C.USER_ID = id AND C.CONTENT_TYPE = 'post' AND C.COMPLETE = 1 AND C.DELETE_DT IS NULL)")
        return 0;
    }

    public long countProjectByUserId(long id) {
        //@Formula("(SELECT COUNT(*) FROM CONTENT C WHERE C.USER_ID = id AND C.CONTENT_TYPE = 'repo' AND C.COMPLETE = 1 AND C.DELETE_DT IS NULL)")
        return 0;
    }
}
