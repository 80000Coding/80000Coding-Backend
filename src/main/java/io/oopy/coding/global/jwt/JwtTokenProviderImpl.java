package io.oopy.coding.global.jwt;

import io.jsonwebtoken.*;
import io.oopy.coding.domain.user.dto.UserAuthenticateDto;
import io.oopy.coding.global.redis.refresh.RefreshToken;
import io.oopy.coding.global.redis.refresh.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProviderImpl implements JwtTokenProvider {
    private static String jwtSecretKey;
    private static int accessTokenExpirationTime;
    private static int refreshTokenExpirationTime;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtTokenProviderImpl(
            @Value("${jwt.secret}") String jwtSecretKey,
            @Value("${jwt.token.access-expiration-time}") int accessTokenExpirationTime,
            @Value("${jwt.token.refresh-expiration-time}") int refreshTokenExpirationTime,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.jwtSecretKey = jwtSecretKey;
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * 헤더로부터 토큰을 추출하는 메서드
     * @param header : 메시지 헤더
     */
    public String resolveToken(String header) throws SignatureException, ExpiredJwtException {
        String token = getTokenFromHeader(header);

        if (!isValidToken(token))
            throw new SignatureException();

        if (isTokenExpired(token))
            return refreshTokenIfNeeded(getUserIdFromToken(token));

        return token;
    }

    /**
     * 사용자 정보 기반으로 액세스 토큰을 생성하는 메서드
     * @param dto UserDto : 사용자 정보
     * @return String : 토큰
     */
    public String generateAccessToken(UserAuthenticateDto dto) {
        log.info("generateAccessToken : {}", dto);
        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(dto))
                .signWith(SignatureAlgorithm.HS256, createSignature())
                .setExpiration(createExpireDate(accessTokenExpirationTime))
                .compact();
    }

    /**
     * 사용자 정보 기반으로 리프레시 토큰을 생성하는 메서드
     * @param dto UserDto : 사용자 정보
     * @return String : 토큰
     */
    public String generateRefreshToken(UserAuthenticateDto dto) {
        log.info("generateRefreshToken : {}", dto);
        String token = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(dto))
                .signWith(SignatureAlgorithm.HS256, createSignature())
                .setExpiration(createExpireDate(refreshTokenExpirationTime))
                .compact();
        log.info("generateRefreshToken : {}", token);
        RefreshToken refreshToken = RefreshToken.of(dto.getId(), token);
        refreshTokenRepository.save(refreshToken, refreshTokenExpirationTime);

        return token;
    }

    private boolean isValidToken(String token) {
        try {
            Claims claims = getClaimsFormToken(token);
            long userId = getUserIdFromToken(token);

            log.info("expireTime: {}", claims.getExpiration());
            log.info("userId: {}", claims.get("userId", Long.class));
            log.info("userName: {}", claims.get("userName", String.class));

            return true;
        } catch (JwtException e) {
            log.error("error message: {}", e.getMessage());
            log.error("Token Tampered");
            return false;
        } catch (NullPointerException e) {
            log.error("Token is null");
            return false;
        }
    }

    private String refreshTokenIfNeeded(Long userId) {
        return refreshTokenRepository.findById(userId).map(
                refreshToken -> {
                    if (isTokenExpired(refreshToken.getToken())) {
                        return generateAccessToken(
                                UserAuthenticateDto.of(refreshToken.getUserId(),
                                        getGithubIdFromToken(refreshToken.getToken()))
                        );
                    } else {
                        return refreshToken.getToken();
                    }
                }
        ).orElseThrow(() -> new ExpiredJwtException(null, null, "Refresh Token is invalid"));
    }

    private boolean isTokenExpired(String token) {
        return getClaimsFormToken(token).getExpiration().before(new Date());
    }

    private static Map<String, Object> createHeader() {
        return Map.of("typ", "JWT",
                "alg", "HS256",
                "regDate", System.currentTimeMillis());
    }

    private static Map<String, Object> createClaims(UserAuthenticateDto dto) {

        return Map.of("userId", dto.getId(), "githubId", dto.getGithubId());
    }

    private static Key createSignature() {
        byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecretKey);
        return new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    private static Date createExpireDate(int expirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, expirationTime);
        return calendar.getTime();
    }

    private static String getTokenFromHeader(String header) {
        if (header == null || !header.startsWith("Bearer "))
            throw new RuntimeException("JWT Token is missing");
        return header.split(" ")[1];
    }

    private static Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFormToken(token);
        return claims.get("userId", Long.class);
    }

    private Integer getGithubIdFromToken(String token) {
        Claims claims = getClaimsFormToken(token);
        return claims.get("githubId", Integer.class);
    }

    private static Claims getClaimsFormToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getDecoder().decode(jwtSecretKey))
                .parseClaimsJws(token)
                .getBody();
    }
}