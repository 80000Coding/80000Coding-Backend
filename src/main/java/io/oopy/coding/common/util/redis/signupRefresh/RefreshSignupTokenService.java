package io.oopy.coding.common.util.redis.signupRefresh;

import io.oopy.coding.common.util.jwt.exception.AuthErrorException;

public interface RefreshSignupTokenService {
    /**
     * access token을 받아서 refresh token을 발행
     * @param accessToken : JwtUserInfo
     * @return String : Refresh Token
     * @throws AuthErrorException : 토큰이 유효하지 않을 경우
     */
    String issueRefreshToken(String accessToken) throws AuthErrorException;

    /**
     * access token 으로 refresh token을 찾아서 제거 (회원가입 완료)
     * @param requestRefreshToken : String
     */
    void signupDone(String requestRefreshToken);
}
