package io.oopy.coding.global.redis.refresh;

import io.oopy.coding.global.jwt.entity.JwtUserInfo;
import io.oopy.coding.global.jwt.exception.auth.AuthErrorException;

public interface RefreshTokenService {
    /**
     * access token을 받아서 refresh token을 발행
     * @param user : JwtUserInfo
     * @return String : Refresh Token
     * @throws AuthErrorException : 토큰이 유효하지 않을 경우
     */
    String issueRefreshToken(String accessToken) throws AuthErrorException;

    /**
     * refresh token을 받아서 refresh token을 재발행
     * @param requestRefreshToken : String
     * @return RefreshToken
     * @throws AuthErrorException : 토큰이 유효하지 않을 경우
     */
    RefreshToken refresh(String requestRefreshToken) throws AuthErrorException;

    /**
     * access token 으로 refresh token을 찾아서 제거 (로그아웃)
     * @param currentUserId : Long
     * @param requestRefreshToken : String
     */
    void logout(Long currentUserId, String requestRefreshToken);
}
