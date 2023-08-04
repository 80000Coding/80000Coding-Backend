package io.oopy.coding.domain.content.entity;

import io.oopy.coding.domain.entity.Auditable;
import io.oopy.coding.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="CONTENT")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Content extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "body")
    private String body;

    @Column(name = "repo_name")
    private String repoName;

    @Column(name = "repo_owner")
    private String repoOwner;

    @Column(name = "views", nullable = false)
    private Long views;

    @Column(name = "delete_dt", nullable = false)
    private LocalDateTime deleteAt;
}
