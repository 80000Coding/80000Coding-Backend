package io.oopy.coding.domain.content.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.oopy.coding.domain.content.entity.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.oopy.coding.domain.content.entity.QContent.content;

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
                .where(content.title.contains(body))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory.select(content.count())
                .from(content)
                .where(content.title.contains(body));
        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }
}
