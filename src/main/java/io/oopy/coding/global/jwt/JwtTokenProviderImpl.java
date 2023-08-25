package io.oopy.coding.global.jwt;

import io.jsonwebtoken.*;
import io.oopy.coding.domain.user.domain.RoleType;
import io.oopy.coding.domain.user.dto.UserAuthenticateReq;
import io.oopy.coding.global.jwt.exception.auth.AuthErrorCode;
import io.oopy.coding.global.jwt.exception.auth.AuthErrorException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {
    private final String jwtSecretKey;
    private final Duration accessTokenExpirationTime;

    public JwtTokenProviderImpl(
            @Value("${jwt.secret}") String jwtSecretKey,
            @Value("${jwt.token.access-expiration-time}") Duration accessTokenExpirationTime
    ) {
        this.jwtSecretKey = jwtSecretKey;
        this.accessTokenExpirationTime = accessTokenExpirationTime;
    }

    @Override
    public String resolveToken(String authHeader) throws AuthErrorException {
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(AuthConstants.TOKEN_TYPE.getValue())) {
            return authHeader.substring(AuthConstants.TOKEN_TYPE.getValue().length());
        }
        throw new AuthErrorException(AuthErrorCode.EMPTY_ACCESS_TOKEN, "Access Token is empty.");
    }

    @SuppressWarnings("deprecation")
    @Override
    public String generateAccessToken(UserAuthenticateReq dto) {
        final Date now = new Date();

        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(dto))
                .signWith(SignatureAlgorithm.HS256, createSignature())
                .setExpiration(createExpireDate(now))
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

    private Claims verifyAndGetClaims(final String accessToken) throws AuthErrorException {
        try {
            return getClaimsFromToken(accessToken);
        } catch (JwtException e) {
            handleJwtException(e);
        }
        throw new IllegalStateException("Unreachable code reached.");
    }

    private void handleJwtException(JwtException e) {
        AuthErrorCode errorCode;
        String causedBy;
        if (e instanceof ExpiredJwtException) {
            errorCode = AuthErrorCode.EXPIRED_ACCESS_TOKEN;
            causedBy = e.toString();
        } else if (e instanceof MalformedJwtException) {
            errorCode = AuthErrorCode.MALFORMED_ACCESS_TOKEN;
            causedBy = e.toString();
        } else if (e instanceof SignatureException) {
            errorCode = AuthErrorCode.TAMPERED_ACCESS_TOKEN;
            causedBy = e.toString();
        } else if (e instanceof UnsupportedJwtException) {
            errorCode = AuthErrorCode.WRONG_JWT_TOKEN;
            causedBy = e.toString();
        } else {
            errorCode = AuthErrorCode.EMPTY_ACCESS_TOKEN;
            causedBy = e.toString();
        }

        log.warn(causedBy);
        throw new AuthErrorException(errorCode, causedBy);
    }

    private Map<String, Object> createHeader() {
        return Map.of("typ", "JWT",
                "alg", "HS256",
                "regDate", System.currentTimeMillis());
    }

    private Map<String, Object> createClaims(UserAuthenticateReq dto) {
        return Map.of("userId", dto.getId(),
                "role", dto.getRole().getRole(),
                "githubId", dto.getGithubId());
    }

    private Key createSignature() {
        byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecretKey);
        return new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    private Date createExpireDate(final Date now) {
        return new Date(now.getTime() + accessTokenExpirationTime.toMillis());
    }

    @SuppressWarnings("deprecation")
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getDecoder().decode(jwtSecretKey))
                .parseClaimsJws(token)
                .getBody();
    }
}