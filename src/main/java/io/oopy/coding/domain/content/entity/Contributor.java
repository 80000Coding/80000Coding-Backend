package io.oopy.coding.domain.content.entity;

import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.model.Auditable;
import io.oopy.coding.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "CONTRIBUTOR")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Contributor extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;

    @ManyToOne
    @JoinColumn(name = "user_id") // 단방향, 문제 생기면 User Entity 에 매핑 추가
    private User user;

}
