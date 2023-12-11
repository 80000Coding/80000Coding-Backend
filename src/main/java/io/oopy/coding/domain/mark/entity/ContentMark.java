package io.oopy.coding.domain.mark.entity;

import io.oopy.coding.api.mark.MarkType;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.model.Auditable;
import io.oopy.coding.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="CONTENT_MARK")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentMark extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "type")
    private MarkType type;
}
