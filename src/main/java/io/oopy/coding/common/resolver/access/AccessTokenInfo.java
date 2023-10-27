package io.oopy.coding.common.resolver.access;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AccessTokenInfoResolver에서 AccessToken 객체를 생성할 때 사용하는 어노테이션
 * @see AccessTokenInfoResolver
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessTokenInfo {
    /**
     * accessToken이 필수인지 여부
     * 기본 값은 true, accessToken이 없다면 AuthErrorException 발생
     * 만약 accessToken이 null일 때, AuthErrorException을 발생시키지 않고 null을 반환하고 싶다면 false로 설정
     */
    boolean required() default true;
}