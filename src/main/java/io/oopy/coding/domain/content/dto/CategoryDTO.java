package io.oopy.coding.domain.content.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoryDTO {
    private Long id;
    private String name;
    private String color;
}
