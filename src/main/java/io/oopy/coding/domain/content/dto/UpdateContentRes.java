package io.oopy.coding.domain.content.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UpdateContentRes {
    private Long contentId;
    private LocalDateTime updatedAt;

    public static UpdateContentRes of(Long contentId, LocalDateTime updatedAt) {
        return new UpdateContentRes(contentId, updatedAt);
    }
}
