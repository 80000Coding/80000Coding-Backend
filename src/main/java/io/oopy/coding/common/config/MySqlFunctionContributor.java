package io.oopy.coding.common.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.spi.TypeConfiguration;

/**
 * MySQL의 match_against 함수를 사용하기 위한 FunctionContributor
 *
 * <pre>
 * Boolean Mode 검색 예시
 * {@code
 * public List<Entity> findAllBy(final String entityNameWord, final String entityAttrWord) {
 *    return queryFactory.selectFrom(entity)
 *        .where(matchAgainst(entity.name, entity.attr, entityAttrWord))
 *        .fetch();
 * }
 *
 * public static BooleanExpression matchAgainst(final StringPath c1, final StringPath c2, final String target) {
 *      if (!StringUtils.hasText(target)) { return null; }
 *      String template = "'" + target + "*'";
 *      return Expressions.booleanTemplate( "function('match_against', {0}, {1}, {2})", c1, c2, template);
 * }
 * </pre>
 *
 * @see <a href="https://velog.io/@ttomy/%EC%82%AC%EC%9A%A9%EC%9E%90-%EC%A0%95%EC%9D%98-dialectmatch-against%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0">참고 블로그</a>
 */
public class MySqlFunctionContributor implements FunctionContributor {
    private static final String ONE_COLUMN_NATURAL_PATTERN = "match(?1) against(?2 in natural language mode)";
    private static final String TWO_COLUMN_NATURAL_PATTERN = "match(?1, ?2) against(?3 in natural language mode)";
    private static final String ONE_COLUMN_BOOLEAN_PATTERN = "match(?1) against(?2 in boolean mode)";
    private static final String TWO_COLUMN_BOOLEAN_PATTERN = "match(?1, ?2) against(?3 in boolean mode)";

    @Override
    public void contributeFunctions(final FunctionContributions functionContributions) {
        SqmFunctionRegistry registry = functionContributions.getFunctionRegistry();
        TypeConfiguration typeConfiguration = functionContributions.getTypeConfiguration();

        registry.registerPattern("one_column_natural", ONE_COLUMN_NATURAL_PATTERN, typeConfiguration.getBasicTypeRegistry().resolve(StandardBasicTypes.BOOLEAN));
        registry.registerPattern("two_column_natural", TWO_COLUMN_NATURAL_PATTERN, typeConfiguration.getBasicTypeRegistry().resolve(StandardBasicTypes.BOOLEAN));
        registry.registerPattern( "one_column_boolean", ONE_COLUMN_BOOLEAN_PATTERN, typeConfiguration.getBasicTypeRegistry().resolve(StandardBasicTypes.BOOLEAN) );
        registry.registerPattern( "two_column_boolean", TWO_COLUMN_BOOLEAN_PATTERN, typeConfiguration.getBasicTypeRegistry().resolve(StandardBasicTypes.BOOLEAN) );
    }
}