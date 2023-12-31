package io.oopy.coding.domain.content.entity;

import io.oopy.coding.domain.model.Auditable;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="CONTENT_CATEGORY")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentCategory extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "category_type", nullable = false)
    private String type;

    public static ContentCategory newInstance(Content content, Category category) {
        return ContentCategory.builder()
                .content(content)
                .category(category)
                .type(category.getName())
                .build();
    }

}
