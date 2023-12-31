package io.oopy.coding.global.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.oopy.coding.common.util.jwt.entity.JwtUserInfo;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class ExpiredTokenGenerator {
    public static String generateExpiredToken(JwtUserInfo dto, String jwtSecretKey) {
        return createExpiredToken(dto, jwtSecretKey);
    }

    private static String createExpiredToken(JwtUserInfo dto, String jwtSecretKey) {
        int expirationTime = -1;

        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(dto))
                .signWith(SignatureAlgorithm.HS256, createSignature(jwtSecretKey))
                .setExpiration(createExpireDate(expirationTime))
                .compact();
    }

    private static Map<String, Object> createHeader() {
        return Map.of("typ", "JWT",
                "alg", "HS256",
                "regDate", System.currentTimeMillis());
    }

    private static Map<String, Object> createClaims(JwtUserInfo dto) {
        return Map.of("userId", dto.getId(),
                "role", dto.getRole());
    }

    private static Key createSignature(String jwtSecretKey) {
        byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecretKey);
        return new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    private static Date createExpireDate(int expirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, expirationTime);
        return calendar.getTime();
    }
}
