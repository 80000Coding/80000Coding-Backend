package io.oopy.coding.common.redis.refresh;

import io.oopy.coding.common.jwt.exception.auth.AuthErrorException;

public interface RefreshTokenService {
    /**
     * access token을 받아서 refresh token을 발행
     * @param accessToken : JwtUserInfo
     * @return String : Refresh Token
     * @throws AuthErrorException : 토큰이 유효하지 않을 경우
     */
    String issueRefreshToken(String accessToken) throws AuthErrorException;

    /**
     * refresh token을 받아서 refresh token을 재발행
     * @param requestRefreshToken : String
     * @return RefreshToken
     * @throws AuthErrorException : 토큰이 유효하지 않을 경우(REFRESH_TOKEN_EXPIRED), 토큰이 탈취당한 경우(REFRESH_TOKEN_MISMATCH)
     */
    RefreshToken refresh(String requestRefreshToken) throws AuthErrorException;

    /**
     * access token 으로 refresh token을 찾아서 제거 (로그아웃)
     * @param requestRefreshToken : String
     */
    void logout(String requestRefreshToken);

    //TODO : 이름을 회원가입 완료 / 회원가입 어떤걸로 할지
    /**
     * access token 으로 refresh token을 찾아서 제거 (회원가입 완료)
     * @param requestRefreshToken : String
     */
    void signupDone(String requestRefreshToken);
}
