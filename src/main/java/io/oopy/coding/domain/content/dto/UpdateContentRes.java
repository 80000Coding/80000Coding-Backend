package io.oopy.coding.domain.content.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class UpdateContentRes {
    private Long content_id;
    private LocalDateTime updated_at;
}
