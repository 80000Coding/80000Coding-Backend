package io.oopy.coding.domain.content.entity;

import io.oopy.coding.domain.entity.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name="CONTENT_CATEGORY")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "type", nullable = false)
    private String type;
}
