package io.oopy.coding.global.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.oopy.coding.domain.user.domain.RoleType;
import io.oopy.coding.domain.user.dto.UserAuthenticateDto;
import org.springframework.stereotype.Service;

@Service("jwtTokenProvider")
public interface JwtTokenProvider {
    Long getUserIdFromToken(String token);
    public RoleType getRoleFromToken(String token);

    String resolveToken(String header) throws ExpiredJwtException;

    String generateAccessToken(UserAuthenticateDto dto);
    String generateRefreshToken(UserAuthenticateDto dto);
}