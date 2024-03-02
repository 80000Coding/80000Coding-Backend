package io.oopy.coding.domain.content.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.oopy.coding.domain.content.entity.Content;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ContentDetailDTO {
    //title ok, body ok, categories join, user join, time ok, views ok, comments ok count, bookmarks join content-mark, likes join content-mark
    private Long id;
    private String title;
    // TODO : take only some strings
    private String body;
    private List<String> categories;
    private String writer;
    private LocalDateTime createdAt;
    private Long views;
    private Integer commentCount;
    private Long likeCount;
    private Long bookmarkCount;

    @QueryProjection
    public ContentDetailDTO(Content content, Long likeCount, Long bookmarkCount)
    {
        this.id = content.getId();
        this.title = content.getTitle();
        this.body = content.getBody();
        this.categories = content.getCategoryNames();
        this.writer = content.getUser().getName();
        this.createdAt = content.getCreatedAt();
        this.views = content.getViews();
        this.commentCount = content.getComments().size();
        this.likeCount = likeCount;
        this.bookmarkCount = bookmarkCount;
    }
//
//    public static ContentDetailDTO toDTO(Content content, Long likeCount, Long bookmarkCount) {
//        return ContentDetailDTO.builder()
//                .id(content.getId())
//                .title(content.getTitle())
//                .body(content.getBody())
//                .categories(content.getCategoryNames())
//                .writer(content.getWriterName())
//                .createdAt(content.getCreatedAt())
//                .views(content.getViews())
//                .commentCount((long)content.getComments().size())
//                .markCount(bookmarkCount)
//                .likeCount(likeCount)
//                .build();
//    }
}
