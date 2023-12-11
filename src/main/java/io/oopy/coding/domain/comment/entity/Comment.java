package io.oopy.coding.domain.comment.entity;

import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.model.Auditable;
import io.oopy.coding.domain.user.entity.User;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="COMMENT")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "content", nullable = false)
    private String commentBody;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "delete_dt")
    private LocalDateTime deleteAt;

    public Comment updateComment(String commentBody) {
        this.commentBody = commentBody;

        return this;
    }

    public void deleteComment() {
        this.deleteAt = LocalDateTime.now();
    }
}
