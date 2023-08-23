package io.oopy.coding.global.jwt;

import io.jsonwebtoken.*;
import io.oopy.coding.domain.user.domain.RoleType;
import io.oopy.coding.domain.user.dto.UserAuthenticateDto;
import io.oopy.coding.global.jwt.exception.auth.AuthErrorCode;
import io.oopy.coding.global.jwt.exception.auth.AuthErrorException;
import io.oopy.coding.global.redis.refresh.RefreshToken;
import io.oopy.coding.global.redis.refresh.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JwtTokenProviderImpl implements JwtTokenProvider {
    private final String jwtSecretKey;
    private final int accessTokenExpirationTime;

    public JwtTokenProviderImpl(
            @Value("${jwt.secret}") String jwtSecretKey,
            @Value("${jwt.token.access-expiration-time}") int accessTokenExpirationTime,
            @Value("${jwt.token.refresh-expiration-time}") int refreshTokenExpirationTime,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.jwtSecretKey = jwtSecretKey;
        this.accessTokenExpirationTime = accessTokenExpirationTime;
    }

    /**
     * 헤더로부터 토큰을 추출하는 메서드
     * @param header : 메시지 헤더
     * @return String : 토큰
     */
    public String resolveToken(String header) throws AuthErrorException {
        String token = getTokenFromHeader(header);
        Claims claims = verifyAndGetClaims(token);

        log.info("resolveToken : {}", token);
        return token;
    }

    /**
     * 사용자 정보 기반으로 액세스 토큰을 생성하는 메서드
     * @param dto UserDto : 사용자 정보
     * @return String : 토큰
     */
    public String generateAccessToken(UserAuthenticateDto dto) {
        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(dto))
                .signWith(SignatureAlgorithm.HS256, createSignature())
                .setExpiration(createExpireDate(accessTokenExpirationTime))
                .compact();
    }

    /**
     * 토큰으로 부터 유저 아이디를 추출하는 메서드
     * @param token String : 토큰
     * @return Long : 유저 아이디
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = verifyAndGetClaims(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 토큰으로 부터 유저 권한을 추출하는 메서드
     * @param token String : 토큰
     * @return RoleType : 유저 권한
     */
    public RoleType getRoleFromToken(String token) {
        Claims claims = verifyAndGetClaims(token);
        String role = claims.get("role", String.class);
        return RoleType.fromString(role);
    }

    /**
     * 토큰으로 부터 Github Id를 추출하는 메서드
     * @param token String : 토큰
     * @return Integer : 깃허브 아이디
     */
    public Integer getGithubIdFromToken(String token) {
        Claims claims = verifyAndGetClaims(token);
        return claims.get("githubId", Integer.class);
    }

    /**
     * 토큰의 만료일을 추출하는 메서드
     * @param token String : 토큰
     * @return Date : 만료일
     */
    public Date getExpiryDate(String token) {
        Claims claims = verifyAndGetClaims(token);
        return claims.getExpiration();
    }

    /**
     * 토큰의 Claim을 추출하는 메서드
     * @throws ExpiredJwtException : 토큰이 만료되었을 경우
     * @throws MalformedJwtException : 토큰이 올바르지 않을 경우
     * @throws SignatureException : 토큰의 서명이 올바르지 않을 경우
     * @throws UnsupportedJwtException : 토큰의 형식이 올바르지 않을 경우
     * @throws IllegalArgumentException : 토큰이 비어있을 경우
     */
    private Claims verifyAndGetClaims(final String accessToken) {
        Claims claims = null;
        try {
            claims = getClaimsFromToken(accessToken);
        } catch (ExpiredJwtException e) {
            log.warn("JWT Expired Error : {}", e.getMessage());
            throw new AuthErrorException(AuthErrorCode.EXPIRED_ACCESS_TOKEN, e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("JWT Malformed Error : {}", e.getMessage());
            throw new AuthErrorException(AuthErrorCode.MALFORMED_ACCESS_TOKEN, e.getMessage());
        } catch (SignatureException e) {
            log.warn("JWT Signature Error : {}", e.getMessage());
            throw new AuthErrorException(AuthErrorCode.TAMPERED_ACCESS_TOKEN, e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT Unsupported Error : {}", e.getMessage());
            throw new AuthErrorException(AuthErrorCode.WRONG_JWT_TOKEN, e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT Illegal Argument Error : {}", e.getMessage());
            throw new AuthErrorException(AuthErrorCode.EMPTY_ACCESS_TOKEN, e.getMessage());
        }
        return claims;
    }

    private Map<String, Object> createHeader() {
        return Map.of("typ", "JWT",
                "alg", "HS256",
                "regDate", System.currentTimeMillis());
    }

    private Map<String, Object> createClaims(UserAuthenticateDto dto) {
        return Map.of("userId", dto.getId(),
                "role", dto.getRole().getRole(),
                "githubId", dto.getGithubId());
    }

    private Key createSignature() {
        byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecretKey);
        return new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    private Date createExpireDate(int expirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, expirationTime);
        return calendar.getTime();
    }

    private String getTokenFromHeader(String header) {
        if (header == null || !header.startsWith("Bearer "))
            throw new AuthErrorException(AuthErrorCode.INVALID_HEADER, "Invalid Header Format");
        return header.substring(AuthConstants.TOKEN_TYPE.getValue().length());
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getDecoder().decode(jwtSecretKey))
                .parseClaimsJws(token)
                .getBody();
    }
}