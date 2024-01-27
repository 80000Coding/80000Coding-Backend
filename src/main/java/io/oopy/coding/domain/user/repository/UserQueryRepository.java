package io.oopy.coding.domain.user.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.entity.ContentType;
import io.oopy.coding.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.oopy.coding.domain.content.entity.QContent.content;
import static io.oopy.coding.domain.user.entity.QUser.user;

@RequiredArgsConstructor
@Repository
public class UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<User> findByNickname(String nickname, Pageable pageable) {
        List<User> fetch = queryFactory.selectFrom(user)
                .where(user.name.contains(nickname))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory.select(user.count())
                .from(user)
                .where(user.name.contains(nickname));
        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    //post project
    public long countPostByUserId(long id) {
        long postCount = queryFactory.selectFrom(content)
                .where(content.user.id.eq(id)
                    .and(content.type.eq(ContentType.POST))
                    .and(content.publish.eq(true))
                    .and(content.deleteAt.isNull())
                )
                .fetchCount();

        return postCount;
    }

    public long countProjByUserId(long id) {
        long postCount = queryFactory.selectFrom(content)
                .where(content.user.id.eq(id)
                        .and(content.type.eq(ContentType.PROJECT))
                        .and(content.publish.eq(true))
                        .and(content.deleteAt.isNull())
                )
                .fetchCount();

        return postCount;
    }
}
