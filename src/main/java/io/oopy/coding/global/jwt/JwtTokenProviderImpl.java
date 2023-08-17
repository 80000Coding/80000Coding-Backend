package io.oopy.coding.global.jwt;

import io.jsonwebtoken.*;
import io.oopy.coding.domain.user.domain.RoleType;
import io.oopy.coding.domain.user.dto.UserAuthenticateDto;
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
     * @return String : 토큰
     */
    public String resolveToken(String header) throws JwtException {
        String token = getTokenFromHeader(header);

        return isTokenExpired(token)
                ? refreshTokenIfNeeded(getUserIdFromToken(token))
                : token;
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
     * 사용자 정보 기반으로 리프레시 토큰을 생성하는 메서드
     * @param dto UserDto : 사용자 정보
     * @return String : 토큰
     */
    public String generateRefreshToken(UserAuthenticateDto dto) {
        String token = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(dto))
                .signWith(SignatureAlgorithm.HS256, createSignature())
                .setExpiration(createExpireDate(refreshTokenExpirationTime))
                .compact();
        RefreshToken refreshToken = RefreshToken.of(dto.getId(), token);
        refreshTokenRepository.save(refreshToken, refreshTokenExpirationTime);

        return token;
    }

    /**
     * 토큰으로 부터 유저 아이디를 추출하는 메서드
     * @param token String : 토큰
     * @return Long : 유저 아이디
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 토큰으로 부터 유저 권한을 추출하는 메서드
     * @param token String : 토큰
     * @return RoleType : 유저 권한
     */
    public RoleType getRoleFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        String role = claims.get("role", String.class);
        return RoleType.fromString(role);
    }

    /**
     * 토큰으로 부터 Github Id를 추출하는 메서드
     * @param token String : 토큰
     * @return Integer : 깃허브 아이디
     */
    public Integer getGithubIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("githubId", Integer.class);
    }

    private String refreshTokenIfNeeded(Long userId) {
        log.info("refreshTokenIfNeeded : {}", userId);
        return refreshTokenRepository.findById(userId).map(
                refreshToken -> {
                    if (!isTokenExpired(refreshToken.getToken())) {
                        return generateAccessToken(
                                UserAuthenticateDto.of(refreshToken.getUserId(),
                                        getRoleFromToken(refreshToken.getToken()))
                        );
                    } else {
                        throw new ExpiredJwtException(null, null, "Refresh Token is expired");
                    }
                }
        ).orElseThrow(() -> new JwtException("Refresh Token is not found"));
    }

    private boolean isTokenExpired(String token) {
        try {
            return getClaimsFromToken(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.error("isTokenExpired : {}", e.getMessage());
            return true;
        }
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
            throw new JwtException("JWT Token is missing");
        return header.split(" ")[1];
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getDecoder().decode(jwtSecretKey))
                .parseClaimsJws(token)
                .getBody();
    }
}