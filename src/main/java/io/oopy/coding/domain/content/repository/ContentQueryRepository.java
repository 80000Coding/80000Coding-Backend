package io.oopy.coding.domain.content.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.oopy.coding.domain.content.dto.ContentDetailDTO;
import io.oopy.coding.domain.content.dto.QContentDetailDTO;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.entity.ContentType;
import io.oopy.coding.domain.mark.entity.MarkType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.oopy.coding.domain.content.entity.QContent.content;
import static io.oopy.coding.domain.mark.entity.QContentMark.contentMark;

@RequiredArgsConstructor
@Repository
public class ContentQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Content> findByTitle(String title, Pageable pageable) {
        List<Content> fetch = queryFactory.selectFrom(content)
                .where(content.title.contains(title))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory.select(content.count())
                .from(content)
                .where(content.title.contains(title));
        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    public Page<Content> findByBody(String body, Pageable pageable) {
        List<Content> fetch = queryFactory.selectFrom(content)
                .where(content.body.contains(body))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory.select(content.count())
                .from(content)
                .where(content.body.contains(body));
        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }


    public Page<ContentDetailDTO> findPostByTitle(String title, Pageable pageable) {
        List<ContentDetailDTO> fetch = queryFactory.select(
                        new QContentDetailDTO(
                                content,
                                JPAExpressions.select(contentMark.count())
                                        .from(contentMark)
                                        .where(contentMark.content.id.eq(content.id), contentMark.type.eq(MarkType.LIKE)),
                                JPAExpressions.select(contentMark.count())
                                        .from(contentMark)
                                        .where(contentMark.content.id.eq(content.id), contentMark.type.eq(MarkType.BOOKMARK))
                        )
                )
                .from(content)
                .where(content.title.contains(title)
                        .and(content.type.eq(ContentType.POST))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory.select(content.count())
                .from(content)
                .where(content.title.contains(title)
                        .and(content.type.eq(ContentType.POST))
                );

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    public Page<ContentDetailDTO> findPostByBody(String body, Pageable pageable) {
        List<ContentDetailDTO> fetch = queryFactory.select(
                        new QContentDetailDTO(
                                content,
                                JPAExpressions.select(contentMark.count())
                                        .from(contentMark)
                                        .where(contentMark.content.id.eq(content.id), contentMark.type.eq(MarkType.LIKE)),
                                JPAExpressions.select(contentMark.count())
                                        .from(contentMark)
                                        .where(contentMark.content.id.eq(content.id), contentMark.type.eq(MarkType.BOOKMARK))
                        )
                )
                .from(content)
                .where(content.body.contains(body)
                        .and(content.type.eq(ContentType.POST))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory.select(content.count())
                .from(content)
                .where(content.body.contains(body)
                        .and(content.type.eq(ContentType.POST))
                );

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    public Page<ContentDetailDTO> findProjectByTitle(String title, Pageable pageable) {
        List<ContentDetailDTO> fetch = queryFactory.select(
                        new QContentDetailDTO(
                                content,
                                JPAExpressions.select(contentMark.count())
                                        .from(contentMark)
                                        .where(contentMark.content.id.eq(content.id), contentMark.type.eq(MarkType.LIKE)),
                                JPAExpressions.select(contentMark.count())
                                        .from(contentMark)
                                        .where(contentMark.content.id.eq(content.id), contentMark.type.eq(MarkType.BOOKMARK))
                        )
                )
                .from(content)
                .where(content.title.contains(title)
                        .and(content.type.eq(ContentType.PROJECT))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory.select(content.count())
                .from(content)
                .where(content.title.contains(title)
                        .and(content.type.eq(ContentType.PROJECT))
                );

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    public Page<ContentDetailDTO> findProjectByBody(String body, Pageable pageable) {
        List<ContentDetailDTO> fetch = queryFactory.select(
                        new QContentDetailDTO(
                                content,
                                JPAExpressions.select(contentMark.count())
                                        .from(contentMark)
                                        .where(contentMark.content.id.eq(content.id), contentMark.type.eq(MarkType.LIKE)),
                                JPAExpressions.select(contentMark.count())
                                        .from(contentMark)
                                        .where(contentMark.content.id.eq(content.id), contentMark.type.eq(MarkType.BOOKMARK))
                        )
                )
                .from(content)
                .where(content.body.contains(body)
                        .and(content.type.eq(ContentType.PROJECT))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory.select(content.count())
                .from(content)
                .where(content.body.contains(body)
                        .and(content.type.eq(ContentType.PROJECT))
                );

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }
}
