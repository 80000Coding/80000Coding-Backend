package io.oopy.coding.domain.user.entity;

import io.oopy.coding.domain.model.Auditable;
import io.oopy.coding.domain.organization.entity.Organization;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="USERS")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name", "email", "role"})
public class User extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Formula("(SELECT COUNT(*) FROM CONTENT C WHERE C.USER_ID = id AND C.CONTENT_TYPE = 'post' AND C.COMPLETE = 1 AND C.DELETE_DT IS NULL)")
    private int postCount;

    @Formula("(SELECT COUNT(*) FROM CONTENT C WHERE C.USER_ID = id AND C.CONTENT_TYPE = 'repo' AND C.COMPLETE = 1 AND C.DELETE_DT IS NULL)")
    private int projectCount;

    @OneToMany
    @JoinTable(
            name = "user_organization",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_id")
    )
    private final List<Organization> organizations = new LinkedList<>();

    private User(Integer githubId, String name) {
        this.githubId = githubId;
        this.name = name;
        this.role = RoleType.USER;
    }

    public static User of(Integer githubId, String name) {
        return new User(githubId, name);
    }
}
