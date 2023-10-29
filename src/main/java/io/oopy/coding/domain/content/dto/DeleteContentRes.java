package io.oopy.coding.domain.content.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DeleteContentRes {
    private Long contentId;
    private LocalDateTime deletedAt;

    public static DeleteContentRes of(Long contentId, LocalDateTime deletedAt) {
        return new DeleteContentRes(contentId, deletedAt);
    }
}
