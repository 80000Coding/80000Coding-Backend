package io.oopy.coding.content.service;

import io.oopy.coding.domain.content.dto.ContentDTO;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.repository.ContentRepository;
import io.oopy.coding.domain.entity.User;
import io.oopy.coding.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentSearchService {
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

    public List<ContentDTO> getUserContents(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist"));

        List<Content> contents = contentRepository.findContentsByUserId(userId);

        List<ContentDTO> response = new ArrayList<>();

        for (Content content : contents) {
            ContentDTO dto = ContentDTO.toDTO(content);
            response.add(dto);
        }

        return response;
    }
}
