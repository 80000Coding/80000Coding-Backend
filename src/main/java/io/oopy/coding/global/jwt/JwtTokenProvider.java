package io.oopy.coding.global.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.oopy.coding.domain.user.domain.RoleType;
import io.oopy.coding.domain.user.dto.UserAuthenticateDto;
import io.oopy.coding.global.jwt.exception.auth.AuthErrorException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Component
public interface JwtTokenProvider {
    /**
     * 헤더로부터 토큰을 추출하고 유효성을 검사하는 메서드
     * @param header : 메시지 헤더
     * @return String : 토큰
     * @throws AuthErrorException : 토큰이 유효하지 않을 경우
     */
    String resolveToken(String header);

    /**
     * 헤더로부터 토큰을 추출하는 메서드
     * @param header : 메시지 헤더
     * @return String : 토큰
     */
    String getTokenFromHeader(String header);

    /**
     * 토큰으로 부터 유저 아이디를 추출하는 메서드
     * @param token String : 토큰
     * @return Long : 유저 아이디
     */
    Long getUserIdFromToken(String token);

    /**
     * 토큰으로 부터 유저 권한을 추출하는 메서드
     * @param token String : 토큰
     * @return RoleType : 유저 권한
     */
    RoleType getRoleFromToken(String token);

    /**
     * 토큰으로 부터 Github Id를 추출하는 메서드
     * @param token String : 토큰
     * @return Integer : 깃허브 아이디
     */
    Integer getGithubIdFromToken(String token);

    /**
     * 토큰의 만료일을 추출하는 메서드
     * @param token String : 토큰
     * @return Date : 만료일
     */
    Date getExpiryDate(String token);

    /**
     * 사용자 정보 기반으로 액세스 토큰을 생성하는 메서드
     * @param dto UserDto : 사용자 정보
     * @return String : 토큰
     */
    String generateAccessToken(UserAuthenticateDto dto);
}