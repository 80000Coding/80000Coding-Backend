package io.oopy.coding.domain.mark.entity;

import io.oopy.coding.domain.entity.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="CONTENT_MARK")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentMark extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private String type;
}
