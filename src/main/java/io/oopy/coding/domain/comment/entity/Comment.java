package io.oopy.coding.domain.comment.entity;

import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="COMMENT")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

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

    @Column(name = "update_dt")
    private LocalDateTime deleteAt;
}
