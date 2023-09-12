package io.oopy.coding.domain.user.entity;

import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.model.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="USERS") // 테이블 생성이 되지 않아 일시적으로 USERS로 변경(기존: USER)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name", "email", "role"})
public class User extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Content> contents = new ArrayList<>();

    @Column(name = "github_id", nullable = false)
    private Integer githubId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "bio")
    private String bio;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "email")
    private String email;

    @Convert(converter = RoleTypeConverter.class)
    @Column(name = "role", nullable = false)
    private RoleType role;
}