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

import static io.oopy.coding.global.jwt.AuthConstants.TOKEN_TYPE;

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

    @Override
    public String resolveToken(String header) throws AuthErrorException {
        String token = getTokenFromHeader(header);
        Claims claims = verifyAndGetClaims(token);

        log.info("token verified. about claims : {}", claims.get("userId", String.class));
        return token;
    }

    @Override
    public String getTokenFromHeader(String header) {
        if (header == null || !header.startsWith(TOKEN_TYPE.getValue()))
            throw new AuthErrorException(AuthErrorCode.INVALID_HEADER, "Invalid Header Format");
        return header.substring(TOKEN_TYPE.getValue().length());
    }

    @Override
    public String generateAccessToken(UserAuthenticateDto dto) {
        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(dto))
                .signWith(SignatureAlgorithm.HS256, createSignature())
                .setExpiration(createExpireDate(accessTokenExpirationTime))
                .compact();
    }

    @Override
    public Long getUserIdFromToken(String token) {
        Claims claims = verifyAndGetClaims(token);
        return claims.get("userId", Long.class);
    }

    @Override
    public RoleType getRoleFromToken(String token) {
        Claims claims = verifyAndGetClaims(token);
        String role = claims.get("role", String.class);
        return RoleType.fromString(role);
    }

    @Override
    public Integer getGithubIdFromToken(String token) {
        Claims claims = verifyAndGetClaims(token);
        return claims.get("githubId", Integer.class);
    }

    @Override
    public Date getExpiryDate(String token) {
        Claims claims = verifyAndGetClaims(token);
        return claims.getExpiration();
    }

    private Claims verifyAndGetClaims(final String accessToken) {
        try {
            return getClaimsFromToken(accessToken);
        } catch (JwtException e) {
            handleJwtException(e);
        }
        throw new IllegalStateException("Unreachable code reached.");
    }

    private void handleJwtException(JwtException e) {
        AuthErrorCode errorCode;
        String errorMessage;
        if (e instanceof ExpiredJwtException) {
            errorCode = AuthErrorCode.EXPIRED_ACCESS_TOKEN;
            errorMessage = "JWT Expired Error: " + e.getMessage();
        } else if (e instanceof MalformedJwtException) {
            errorCode = AuthErrorCode.MALFORMED_ACCESS_TOKEN;
            errorMessage = "JWT Malformed Error: " + e.getMessage();
        } else if (e instanceof SignatureException) {
            errorCode = AuthErrorCode.TAMPERED_ACCESS_TOKEN;
            errorMessage = "JWT Signature Error: " + e.getMessage();
        } else if (e instanceof UnsupportedJwtException) {
            errorCode = AuthErrorCode.WRONG_JWT_TOKEN;
            errorMessage = "JWT Unsupported Error: " + e.getMessage();
        } else {
            errorCode = AuthErrorCode.EMPTY_ACCESS_TOKEN;
            errorMessage = "JWT Illegal Argument Error: " + e.getMessage();
        }

        log.warn(errorMessage);
        throw new AuthErrorException(errorCode, errorMessage);
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

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getDecoder().decode(jwtSecretKey))
                .parseClaimsJws(token)
                .getBody();
    }
}