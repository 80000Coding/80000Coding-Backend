package io.oopy.coding.global.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.oopy.coding.domain.user.dto.UserAuthenticateDto;
import org.springframework.stereotype.Service;

import java.security.SignatureException;

@Service("jwtTokenProvider")
public interface JwtTokenProvider {
    // 해당 인터페이스 구현체는 JwtTokenProvider 클래스에서 구현한다.
    String resolveToken(String header) throws SignatureException, ExpiredJwtException;

    String generateAccessToken(UserAuthenticateDto dto);
    String generateRefreshToken(UserAuthenticateDto dto);
}