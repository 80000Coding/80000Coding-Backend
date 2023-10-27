package io.oopy.coding.domain.entity;

import io.oopy.coding.domain.content.entity.Content;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="USERS")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends Auditable{
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

    @Column(name = "role", nullable = false)
    private String role;
}
