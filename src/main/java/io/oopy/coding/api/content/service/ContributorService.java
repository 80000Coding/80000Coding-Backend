package io.oopy.coding.api.content.service;

import io.oopy.coding.api.content.exception.ContentErrorCode;
import io.oopy.coding.api.content.exception.ContentErrorException;
import io.oopy.coding.common.util.jwt.exception.AuthErrorCode;
import io.oopy.coding.common.util.jwt.exception.AuthErrorException;
import io.oopy.coding.domain.content.dto.GetAllContributorRes;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.entity.ContentType;
import io.oopy.coding.domain.content.entity.Contributor;
import io.oopy.coding.domain.content.repository.ContentRepository;
import io.oopy.coding.domain.content.repository.ContributorRepository;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContributorService {
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final ContentService contentService;
    private final ContributorRepository contributorRepository;

    public boolean isExist(Long userId, Long contentId) {
        return contributorRepository.existsByUserIdAndContentId(userId, contentId);
    }

    @Transactional
    public List<GetAllContributorRes> getAllContributors(Long contentId) {
        List<Contributor> contributors = contributorRepository.findAllByContentId(contentId);
        if (contributors == null) {
            return null;
        }

        List<GetAllContributorRes> response = new ArrayList<>();

        for (Contributor contributor : contributors) {
            GetAllContributorRes dto = GetAllContributorRes.fromUser(contributor.getUser());
            response.add(dto);
        }

        return response;
    }

    public void addContributor(Long contentId, Long userId) {
        Content content = contentService.findContent(contentId);
        if (!content.getType().equals(ContentType.PROJECT)) {
            throw new ContentErrorException(ContentErrorCode.INVALID_CONTENT_TYPE);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthErrorException(AuthErrorCode.USER_NOT_FOUND, "유저가 존재하지 않습니다."));

        Contributor contributor = Contributor.builder()
                .content(content)
                .user(user)
                .build();

        contributorRepository.save(contributor);
    }

    public void deleteContributor(Long contentId, Long userId) {
        if (!contentRepository.existsById(contentId)) {
            throw new ContentErrorException(ContentErrorCode.INVALID_CONTENT_TYPE);
        } else if (!userRepository.existsById(userId)) {
            throw new AuthErrorException(AuthErrorCode.USER_NOT_FOUND, "유저가 존재하지 않습니다.");
        }
        contributorRepository.deleteByContentIdAndUserId(contentId, userId);
    }
}
