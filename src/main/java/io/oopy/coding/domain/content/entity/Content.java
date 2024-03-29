package io.oopy.coding.domain.content.entity;

import io.oopy.coding.domain.comment.entity.Comment;
import io.oopy.coding.domain.model.Auditable;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.domain.mark.entity.ContentMark;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name="CONTENT")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "content")
    private List<ContentCategory> contentCategories = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "content")
    private List<ContentMark> contentMarks = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "content")
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "content_type", nullable = false)
    @Convert(converter = ContentTypeConverter.class)
    private ContentType type;

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

    @Column(name = "publish", nullable = false)
    private Boolean publish;

    @Column(name = "content_image_url")
    private String contentImageUrl;

    @Column(name = "delete_dt")
    private LocalDateTime deleteAt;

    public Content update(String title, String body, String contentImageUrl, boolean publish) {
        this.title = title;
        this.body = body;
        this.contentImageUrl = contentImageUrl;
        this.publish = publish;

        return this;
    }

    public void softDelete() {
        this.deleteAt = LocalDateTime.now();
    }

    public void plusViewCount() {
        this.views += 1;
    }

    public List<String> getCategoryNames() {
        if (this.contentCategories == null) {
            return new ArrayList<>();
        }
        return this.contentCategories.stream()
                .map(ContentCategory::getCategory)
                .map(Category::getName)
                .collect(Collectors.toList());
    }
}
