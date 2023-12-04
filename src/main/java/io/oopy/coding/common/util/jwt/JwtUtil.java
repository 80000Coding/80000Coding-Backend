package io.oopy.coding.common.util.jwt;

import io.oopy.coding.common.util.jwt.entity.JwtUserInfo;
import io.oopy.coding.common.util.jwt.exception.AuthErrorException;

import java.time.LocalDateTime;

public interface JwtUtil {
    /**
     * 헤더로부터 토큰을 추출하고 유효성을 검사하는 메서드
     * @param authHeader : 메시지 헤더
     * @return 값이 있다면 토큰, 없다면 빈 문자열 (빈 문자열을 반환하는 경우 예외 처리를 해주어야 한다.)
     */
    String resolveToken(String authHeader);

    /**
     * 사용자 정보 기반으로 액세스 토큰을 생성하는 메서드
     * @param user UserDto : 사용자 정보
     * @return String : 토큰
     */
    String generateAccessToken(JwtUserInfo user);

    /**
     * 사용자 정보 기반으로 리프레시 토큰을 생성하는 메서드
     * @param user UserDto : 사용자 정보
     * @return String : 토큰
     */
    String generateRefreshToken(JwtUserInfo user);

    /**
     * github id로 만든 임시 사용자 정보 기반으로 회원가입만을 위한 액세스 토큰을 생성하는 메서드
     * @param user UserDto : 사용자 정보
     * @return String : 토큰
     */
    String generateSignupAccessToken(JwtUserInfo user);

    /**
     * token으로 부터 사용자 정보를 추출하는 메서드
     * @param token String : 토큰
     * @return UserAuthenticateReq : 사용자 정보
     * @throws AuthErrorException : 토큰이 유효하지 않을 경우
     */
    JwtUserInfo getUserInfoFromToken(String token) throws AuthErrorException;

    /**
     * 토큰으로 부터 유저 아이디를 추출하는 메서드
     * @param token String : 토큰
     * @return Long : 유저 아이디
     * @throws AuthErrorException : 토큰이 유효하지 않을 경우
     */
    Long getUserIdFromToken(String token) throws AuthErrorException;

    /**
     * 토큰으로 부터 유저 아이디를 추출하는 메서드
     * @param token String : 토큰
     * @return Integer : 깃허브 유저 아이디
     * @throws AuthErrorException : 토큰이 유효하지 않을 경우
     */
    Integer getGithubIdFromToken(String token) throws AuthErrorException;

    /**
     * 토큰의 만료일을 추출하는 메서드
     * @param token String : 토큰
     * @return LocalDateTime : 만료일
     * @throws AuthErrorException : 토큰이 유효하지 않을 경우
     */
    LocalDateTime getExpiryDate(String token) throws AuthErrorException;

    /**
     * 토큰의 만료 여부를 검사하는 메서드
     * @param token String : 토큰
     */
    boolean isTokenExpired(String token);
}