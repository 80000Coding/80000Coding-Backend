package io.oopy.coding.global.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.oopy.coding.domain.user.domain.RoleType;
import io.oopy.coding.domain.user.dto.UserAuthenticateDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Component
public interface JwtTokenProvider {
    Long getUserIdFromToken(String token);
    RoleType getRoleFromToken(String token);
    Date getExpiryDate(String token);

    String resolveToken(String header) throws ExpiredJwtException;

    String generateAccessToken(UserAuthenticateDto dto);
}