package io.oopy.coding.global.redis.refresh;

public interface RefreshTokenService {
    /**
     * access token을 받아서 refresh token을 발행
     * @param userId : Long
     * @return String : Refresh Token
     */
    String issueRefreshToken(Long userId);

    /**
     * refresh token을 받아서 refresh token을 재발행
     * @param requestRefreshToken : String
     * @return RefreshToken
     */
    RefreshToken refresh(String requestRefreshToken);

    /**
     * access token 으로 refresh token을 찾아서 제거 (로그아웃)
     * @param currentUserId : Long
     * @param requestRefreshToken : String
     */
    void logout(Long currentUserId, String requestRefreshToken);
}
