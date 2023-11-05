package io.oopy.coding.domain.user.entity;

import io.oopy.coding.domain.model.Auditable;
import io.oopy.coding.domain.organization.entity.UserOrganization;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "user")
    private final List<UserOrganization> userOrganizations = new ArrayList<>();

    private User(Integer githubId, String name) {
        this.githubId = githubId;
        this.name = name;
        this.role = RoleType.USER;
    }
    public static User of(Integer githubId, String name) {
        return new User(githubId, name);
    }

    // TODO : front랑 어떻게 값을 바꿀지
    public List<String> getOrganizationCodes() {
        if (this.userOrganizations == null) {
            return new ArrayList<>();
        }

//        List<String> organizationCodes = new ArrayList<>();
//
//        this.userOrganizations.forEach(
//                e -> organizationCodes.add(e.getOrganization().getCode())
//        );

        // TODO : query 횟수 확인 N + 1, lamda method 참조
        return this.userOrganizations
                .stream()
                .map(v-> {
                    return v.getOrganization().getCode();
                }).toList();

        //return organizationCodes;
    }
}
