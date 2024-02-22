package io.oopy.coding.common.util;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.util.StringUtils;

public class QueryDslUtil {
    /**
     * match_against 함수를 통해 c1 컬럼의 target을 natural mode로 탐색한다.
     * @param c1 : 검색할 컬럼
     * @param target : 검색어
     * @return : BooleanExpression (target이 null이거나 공백 문자열이면 null 반환)
     * <pre>
     * {@code
     * public Entity findTarget(String target) {
     *      QEntity entity = QEntity.entity;
     *
     *      return queryFactory
     *             .fromSelect(entity)
     *             .where(QueryDslUtil.matchAgainstNaturalMode(entity.name, target));
     * }
     * }
     * </pre>
     */
    public static BooleanExpression matchAgainstNaturalMode(final StringPath c1, final String target) {
        if (!StringUtils.hasText(target)) { return null; }
        return Expressions.booleanTemplate( "function('one_column_natural', {0}, {1})", c1, target);
    }

    /**
     * match_against 함수를 통해 c1, c2 컬럼의 target을 natural mode로 탐색한다.
     * @param c1 : 검색할 컬럼1
     * @param c2 : 검색할 컬럼2
     * @param target : 검색어
     * @return : BooleanExpression (target이 null이거나 공백 문자열이면 null 반환)
     * <pre>
     * {@code
     * public Entity findTarget(String target) {
     *   QEntity entity = QEntity.entity;
     *
     *   return queryFactory
     *       .fromSelect(entity)
     *       .where(QueryDslUtil.matchAgainstNaturalMode(entity.name, entity.attr, target));
     * }
     * }
     */
    public static BooleanExpression matchAgainstNaturalMode(final StringPath c1, final StringPath c2, final String target) {
        if (!StringUtils.hasText(target)) { return null; }
        String template = "'" + target + "*'";
        return Expressions.booleanTemplate( "function('two_column_natural', {0}, {1}, {2})", c1, c2, template);
    }

    /**
     * match_against 함수를 통해 c1 컬럼의 target을 boolean mode로 탐색한다.
     * @param c1 : 검색할 컬럼
     * @param target : 검색어
     * @return : BooleanExpression (target이 null이거나 공백 문자열이면 null 반환)
     * <pre>
     * {@code
     * public Entity findTarget(String target) {
     *     QEntity entity = QEntity.entity;
     *
     *     return queryFactory
     *     .fromSelect(entity)
     *     .where(QueryDslUtil.matchAgainstBooleanMode(entity.name, target));
     * }
     * }
     * </pre>
     */
    public static BooleanExpression matchAgainstBooleanMode(final StringPath c1, final String target) {
        if (!StringUtils.hasText(target)) { return null; }
        String template = "'" + target + "*'";
        return Expressions.booleanTemplate( "function('one_column_boolean', {0}, {1})", c1, template);
    }

    /**
     * match_against 함수를 통해 c1, c2 컬럼의 target을 boolean mode로 탐색한다.
     * @param c1 : 검색할 컬럼1
     * @param c2 : 검색할 컬럼2
     * @param target : 검색어
     * @return : BooleanExpression (target이 null이거나 공백 문자열이면 null 반환)
     * <pre>
     * {@code
     * public Entity findTarget(String target) {
     *     QEntity entity = QEntity.entity;
     *
     *     return queryFactory
     *     .fromSelect(entity)
     *     .where(QueryDslUtil.matchAgainstBooleanMode(entity.name, entity.attr, target));
     * }
     * }
     * </pre>
     */
    public static BooleanExpression matchAgainstBooleanMode(final StringPath c1, final StringPath c2, final String target) {
        if (!StringUtils.hasText(target)) { return null; }
        String template = "'" + target + "*'";
        return Expressions.booleanTemplate( "function('two_column_boolean', {0}, {1}, {2})", c1, c2, template);
    }
}