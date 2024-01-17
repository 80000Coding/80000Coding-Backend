package io.oopy.coding.common.security.jwt.strategy;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.oopy.coding.common.security.jwt.JwtProvider;
import io.oopy.coding.common.security.jwt.dto.JwtSubInfo;
import io.oopy.coding.common.security.jwt.exception.AuthErrorException;
import io.oopy.coding.common.security.jwt.qualifier.AccessTokenQualifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;

@Slf4j
@Component
@Primary
@AccessTokenQualifier
public class AccessTokenProvider implements JwtProvider {
    private final Key signatureKey;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final Duration tokenExpiration;

    public AccessTokenProvider(
        @Value("${jwt.secret.access}") String jwtSecretKey,
        @Value("${jwt.token.access-expiration-time}") Duration tokenExpiration
    ) {
        final byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecretKey);
        this.signatureKey = Keys.hmacShaKeyFor(secretKeyBytes);
        this.tokenExpiration = tokenExpiration;
    }

    @Override
    public String generateToken(JwtSubInfo subs) {
        return null;
    }

    @Override
    public JwtSubInfo getSubInfoFromToken(String token) {
        return null;
    }

    @Override
    public Claims getClaimsFromToken(String token) {
        return null;
    }

    @Override
    public LocalDateTime getExpiryDate(String token) throws AuthErrorException {
        return null;
    }

    @Override
    public boolean isTokenExpired(String token) {
        return false;
    }
}
