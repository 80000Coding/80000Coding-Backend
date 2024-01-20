package io.oopy.coding.common.security.authorization;

import io.oopy.coding.common.security.authentication.CustomUserDetails;
import io.oopy.coding.domain.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorManager {

    private final ContentRepository contentRepository;

    public boolean isContentAuthor(CustomUserDetails userDetails, Long contentId) {
        return contentRepository.existsByIdAndUserId(userDetails.getUserId(), contentId);
    }
}
