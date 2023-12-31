package io.oopy.coding.domain.user.entity;

import io.oopy.coding.domain.model.Auditable;
import io.oopy.coding.domain.organization.entity.Organization;
import io.oopy.coding.domain.organization.entity.UserOrganization;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @OneToMany(mappedBy = "user")
    private final List<UserOrganization> userOrganizations = new ArrayList<>();

    @Column(name = "deleted")
    private Boolean deleted;

    public void changeNickname(String nickname) {
        this.name = nickname;
    }

    public void setDeleted() {
        this.deleted = Boolean.TRUE;
    }

    private User(Integer githubId, String name) {
        this.githubId = githubId;
        this.name = name;
        this.role = RoleType.USER;
    }
    public static User of(Integer githubId, String name) {
        return new User(githubId, name);
    }

    public void changeProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public List<String> getOrganizationCodes() {
        if (this.userOrganizations == null) {
            return new ArrayList<>();
        }
        return this.userOrganizations.stream()
                .map(UserOrganization::getOrganization)
                .map(Organization::getCode)
                .collect(Collectors.toList());
    }

    public List<String> getOrganizationNames() {
        if (this.userOrganizations == null) {
            return new ArrayList<>();
        }
        return this.userOrganizations.stream()
                .map(UserOrganization::getOrganization)
                .map(Organization::getName)
                .collect(Collectors.toList());
    }
}
