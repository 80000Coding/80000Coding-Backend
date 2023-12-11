package io.oopy.coding.domain.organization.entity;

import io.oopy.coding.domain.model.Auditable;
import io.oopy.coding.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="USER_ORGANIZATION")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserOrganization extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "defaults")
    private Integer defaults;

    @Column(name = "email")
    private String email;

    public UserOrganization(Organization organization, User user, String email) {
        this.organization = organization;
        this.user = user;
        this.email = email;
    }

    public static UserOrganization of(Organization organization, User user, String email) {
        return new UserOrganization(organization, user, email);
    }

    public Organization getOrganization() {
        return this.organization;
    }
}
