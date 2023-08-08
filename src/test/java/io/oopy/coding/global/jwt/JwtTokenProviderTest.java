package io.oopy.coding.global.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.oopy.coding.domain.user.dto.UserAuthenticateDto;
import io.oopy.coding.global.redis.refresh.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {
    private JwtTokenProvider JwtTokenProvider;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private UserAuthenticateDto dto;
    private static final String jwtSecretKey = "exampleSecretKeyForSpringBootProjectAtSubRepository";

    @BeforeEach
    public void setUp() {
        JwtTokenProvider = new JwtTokenProviderImpl(jwtSecretKey, 1, 1, refreshTokenRepository);
        dto = createDto();
    }

    @Test
    public void testGenerateAccessToken() {
        // when
        String accessToken = JwtTokenProvider.generateAccessToken(dto);

        // then
        System.out.println("accessToken : " + accessToken);
        assertNotNull(accessToken);
    }

    @Test
    public void testGenerateRefreshToken() {
        // when
        String refreshToken = JwtTokenProvider.generateRefreshToken(dto);

        // then
        System.out.println("refreshToken : " + refreshToken);
        assertNotNull(refreshToken);
    }

    @Test
    public void testIsTokenExpired() {
        // given
        String header = "Bearer " + ExpiredTokenGenerator.generateExpiredToken(dto, jwtSecretKey);

        // when
        assertThrows(RuntimeException.class, () -> JwtTokenProvider.resolveToken(header));
    }

    @Test
    public void testAccessTokenExpiredAndRefreshTokenValid() {
        // given
        String header = "Bearer " + ExpiredTokenGenerator.generateExpiredToken(dto, jwtSecretKey);
        String refreshToken = JwtTokenProvider.generateRefreshToken(dto);
        ReflectionTestUtils.setField(JwtTokenProvider, "refreshTokenExpirationTime", 1000000);

        // when
        JwtTokenProvider.resolveToken(header);

        // then
        assertNotNull(refreshTokenRepository.findById(dto.getId()).orElse(null));
    }

    @Test
    public void testAccessTokenExpiredAndRefreshTokenExpired() {
    }

    @Test
    public void testReceiveUserIdFromToken() {
        // given
        String token = JwtTokenProvider.generateAccessToken(dto);

        // when
        Long userId = ReflectionTestUtils.invokeMethod(JwtTokenProvider, "getUserIdFromToken", token);

        // then
        System.out.println("userId : " + userId);
        assertEquals(dto.getId(), userId);
    }

    @Test
    public void testReceiveGithubIdFromToken() {
        // given
        String token = JwtTokenProvider.generateAccessToken(dto);

        // when
        Integer githubId = ReflectionTestUtils.invokeMethod(JwtTokenProvider, "getGithubIdFromToken", token);

        // then
        System.out.println("githubId : " + githubId);
        assertEquals(dto.getGithubId(), githubId);
    }

    private UserAuthenticateDto createDto() {
        return UserAuthenticateDto.of(1L, 1);
    }
}